package com.AnalysisAPIserver.domain.client.service;

import com.AnalysisAPIserver.domain.DB_Table.entity.ApiCategory;
import com.AnalysisAPIserver.domain.DB_Table.entity.ApiUser;
import com.AnalysisAPIserver.domain.DB_Table.entity.AppCategory;
import com.AnalysisAPIserver.domain.DB_Table.entity.Application;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiCategoryRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApiUserRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.AppCategoryRepository;
import com.AnalysisAPIserver.domain.DB_Table.repository.ApplicationRepository;
import com.AnalysisAPIserver.domain.client.dto.ClientCreateRequest;
import com.AnalysisAPIserver.domain.client.dto.ClientCreateResponse;
import com.AnalysisAPIserver.domain.client.dto.ClientDetailResponseDto;
import com.AnalysisAPIserver.domain.client.dto.ClientListResponseDto;
import com.AnalysisAPIserver.domain.client.dto.ClientResponse;
import com.AnalysisAPIserver.domain.client.exception.ClientErrorCode;
import com.AnalysisAPIserver.domain.client.exception.ClientException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 클라이언트(애플리케이션) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 클라이언트 생성, 조회, 수정, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    /**
     * API 사용자 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApiUserRepository apiUserRepository;
    /**
     * 애플리케이션(클라이언트) 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApplicationRepository applicationRepository;
    /**
     * 앱 카테고리 정보에 접근하기 위한 리포지토리입니다.
     */
    private final AppCategoryRepository appCategoryRepository;
    /**
     * API 카테고리 정보에 접근하기 위한 리포지토리입니다.
     */
    private final ApiCategoryRepository apiCategoryRepository;

    /**
     * 알 수 없는 카테고리를 나타내는 ID.
     */
    private static final String UNKNOWN_CATEGORY_ID = "UNKNOWN";
    /**
     * 클라이언트의 기본 상태.
     */
    private static final String DEFAULT_CLIENT_STATUS = "ACTIVE";


    /**
     * 새로운 클라이언트(애플리케이션)를 생성합니다.
     *
     * @param request 클라이언트 생성 요청 정보를 담은 DTO.
     * @return 생성된 클라이언트 정보(ID, 앱 이름)를 담은 DTO.
     * @throws ClientException 개발자 또는 카테고리 정보를 찾을 수 없는 경우 발생.
     */
    @Transactional
    public ClientCreateResponse
    createClient(final ClientCreateRequest request) {

        ApiUser developer = apiUserRepository
                .findById(request.getDeveloperId())
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode
                                .OWNER_NOT_FOUND));

        AppCategory appCategory = appCategoryRepository
                .findById(request.getAppCategoryId())
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode
                                .CATEGORY_NOT_FOUND));

        ApiCategory apiCategory = apiCategoryRepository
                .findById(request.getApiCategoryId())
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode
                                .CATEGORY_NOT_FOUND));


        String clientId = UUID.randomUUID().toString();
        String clientSecret = UUID.randomUUID().toString();

        Application application = Application.builder()
                .owner(developer)
                .appCategory(appCategory)
                .apiCategory(apiCategory)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .appName(request.getAppName())
                .callbackUrl(request.getCallbackUrl())

                .build();

        applicationRepository.save(application);

        return new ClientCreateResponse(clientId,
                request.getAppName());

    }

    /**
     * 특정 개발자가 소유한 클라이언트(애플리케이션) 목록을
     * 상세 정보와 함께 조회합니다.
     *
     * @param ownerId 클라이언트 목록을 조회할 개발자의 ID.
     * @return 해당 개발자의 클라이언트 목록
     * (각 클라이언트는 {@link ClientListResponseDto} 형태).
     * @throws ClientException 개발자 정보를 찾을 수 없는 경우 발생.
     */
    @Transactional(readOnly = true)
    public List<ClientListResponseDto>
    getDetailedClientsByOwner(final Long ownerId) {
        ApiUser owner = apiUserRepository
                .findById(ownerId)
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode.OWNER_NOT_FOUND));

        List<Application> apps
                = applicationRepository
                .findAllByOwner(owner);

        return apps.stream()
                .map(app
                        -> ClientListResponseDto.builder()
                        .appName(app.getAppName())
                        .clientId(app.getClientId())
                        .apiCategoryId(app.getApiCategory() != null
                                ? String.valueOf(
                                app.getApiCategory().getApiCategoryId())
                                : UNKNOWN_CATEGORY_ID) // LineLength 해결
                        .appCategoryId(app.getAppCategory() != null
                                ? String.valueOf(
                                app.getAppCategory().getAppCategoryId())
                                : UNKNOWN_CATEGORY_ID)
                        .status(DEFAULT_CLIENT_STATUS)
                        .createAt(app.getCreatedAt().toLocalDate())
                        .build())
                .toList();
    }

    /**
     * 특정 클라이언트 ID에 해당하는 클라이언트의 상세 정보를 조회합니다.
     *
     * @param clientId 조회할 클라이언트의 ID.
     * @return 해당 클라이언트의 상세 정보를 담은 {@link ClientDetailResponseDto}.
     * @throws ClientException 애플리케이션 정보를 찾을 수 없는 경우 발생.
     */
    @Transactional(readOnly = true)
    public ClientDetailResponseDto getClientDetail(final String clientId) {
        Application app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode
                                .APPLICATION_NOT_FOUND));

        return ClientDetailResponseDto.builder()
                .clientId(app.getClientId())
                .clientSecret(app.getClientSecret())
                .appName(app.getAppName())
                .status(DEFAULT_CLIENT_STATUS)
                .createdAt(app.getCreatedAt().toLocalDate())
                .apiCategoryId(String.format("%04d",
                        app.getApiCategory()
                                .getApiCategoryId()))
                .appCategoryId(String.format("%04d",
                        app.getAppCategory()
                                .getAppCategoryId()))
                .build();
    }

    /**
     * 특정 클라이언트(애플리케이션)의 이름을 수정합니다.
     *
     * @param clientId 수정할 클라이언트의 ID.
     * @param newName 새로운 애플리케이션 이름.
     * @return 수정된 클라이언트 정보(ID, 앱 이름)를 담은 {@link ClientResponse}.
     * @throws ClientException 클라이언트 정보를 찾을 수 없는 경우 발생.
     */
    @Transactional
    public ClientResponse updateClientName(final String clientId,
                                           final String newName) {
        Application app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() ->
                        new ClientException(ClientErrorCode.CLIENT_NOT_FOUND));
        app.setAppName(newName);
        applicationRepository.save(app);
        return new ClientResponse(app.getClientId(), app.getAppName());
    }

    /**
     * 여러 클라이언트(애플리케이션)를 한 번에 삭제합니다.
     *
     * @param clientIds 삭제할 클라이언트 ID 목록.
     * @throws ClientException 삭제 대상 클라이언트 중 일부를 찾을 수 없는 경우 발생.
     */
    @Transactional
    public void deleteClients(final List<String> clientIds) {
        List<Application> apps =
                applicationRepository.findAllByClientIdIn(clientIds);

        if (apps.size() != clientIds.size()) {

            throw new ClientException(ClientErrorCode.CLIENT_NOT_FOUND);
        }

        applicationRepository.deleteAll(apps); // LineLength 해결
    }
}
