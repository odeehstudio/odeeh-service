package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;

import java.util.List;

public interface FeedActivityServicePort {

    List<AttendanceEntityQuery> fetchFeed(String uid, Integer page);
}
