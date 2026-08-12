package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.external.InfoDto;
import mate.academy.rickandmorty.dto.external.ResponsePersonageDto;
import mate.academy.rickandmorty.dto.external.ResponseResultsDto;
import mate.academy.rickandmorty.exception.EmptyDataFromApiException;
import mate.academy.rickandmorty.exception.FetchingDataFromApiException;
import mate.academy.rickandmorty.mapper.PersonageMapper;
import mate.academy.rickandmorty.model.Personage;
import mate.academy.rickandmorty.repository.PersonageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterClient {
    private static final String CHARACTER_URL = "https://rickandmortyapi.com/api/character";
    private final PersonageMapper personageMapper;
    private final PersonageRepository repository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private ResponseResultsDto fetchPage(String url) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        ResponseResultsDto responseResultsDto;

        try {
            HttpResponse<String> response = httpClient.send(
                    httpRequest, HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new FetchingDataFromApiException(
                        "R&M API returned status: " + response.statusCode()
                );
            }

            responseResultsDto = objectMapper.readValue(response.body(), ResponseResultsDto.class);
        } catch (IOException | InterruptedException e) {
            throw new FetchingDataFromApiException("Can't fetch data from R&M API: ", e);
        }

        return responseResultsDto;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fetchAllPersonages() {
        ResponseResultsDto responseResultsDto = fetchPage(CHARACTER_URL);
        InfoDto info = responseResultsDto.getInfo();
        List<ResponsePersonageDto> results = responseResultsDto.getResults();

        while (true) {
            if (results.isEmpty()) {
                throw new EmptyDataFromApiException("Fetched data is empty");
            }

            Map<Long, Personage> batchFromPage = getBatchFromPage(results);
            saveBatch(results, batchFromPage);

            if (info.next() == null) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FetchingDataFromApiException(
                        "Interrupted while waiting between API requests", e);
            }

            responseResultsDto = fetchPage(info.next());
            info = responseResultsDto.getInfo();
            results = responseResultsDto.getResults();
        }

    }

    private Map<Long, Personage> getBatchFromPage(List<ResponsePersonageDto> page) {
        Map<Long, Personage> batchMap = new HashMap<>();

        List<Personage> existingPersonages = repository.findAllByExternalIdIn(page.stream()
                .map(ResponsePersonageDto::getExternalId)
                .toList());

        for (Personage personage : existingPersonages) {
            batchMap.put(personage.getExternalId(), personage);
        }

        return batchMap;
    }

    private void saveBatch(List<ResponsePersonageDto> page, Map<Long, Personage> batch) {
        List<Personage> toSave = new ArrayList<>();

        for (ResponsePersonageDto responseDto : page) {
            Personage personage = batch.get(responseDto.getExternalId());

            if (personage == null) {
                toSave.add(personageMapper.toModel(responseDto));
                continue;
            }

            if (isUpToDate(responseDto, personage)) {
                continue;
            }
            toSave.add(toUpdate(responseDto, personage));
        }

        repository.saveAll(toSave);
    }

    private boolean isUpToDate(ResponsePersonageDto dto, Personage model) {
        if (!dto.getName().equals(model.getName())) {
            return false;
        }
        if (!dto.getGender().equals(model.getGender())) {
            return false;
        }
        return dto.getStatus().equals(model.getStatus());
    }

    private Personage toUpdate(ResponsePersonageDto dto, Personage model) {
        model.setName(dto.getName());
        model.setGender(dto.getGender());
        model.setStatus(dto.getStatus());
        return model;
    }

}
