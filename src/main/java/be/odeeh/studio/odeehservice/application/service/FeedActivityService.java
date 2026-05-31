package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.FeedActivityJpaRepository;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.FeedActivityServicePort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FeedActivityService implements FeedActivityServicePort {

    private final FeedActivityJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;
    private final Integer PAGE_SIZE = 20;

    @Override
    public List<AttendanceEntityQuery> fetchFeed(String uid, Integer page) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        return repository.findFeedForUser(authenticatedUser.getId(), pageable).getContent();
    }
}
