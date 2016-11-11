package com.pernix_central.repository;
import com.pernix_central.domain.Activity;
import com.pernix_central.domain.Participation;
import com.pernix_central.domain.ScoreboardAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
@SuppressWarnings("unused")
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query("select activity from Activity activity where activity.coordinator.login = ?#{principal.username}")
    List<Activity> findByCoordinatorIsCurrentUser();

    List<Activity> findByName(@Param("name")String name);

    @Query("SELECT new com.pernix_central.domain.ScoreboardAnswer(user.firstName, user.lastName, SUM(activity.points))\n " +
        "from Participation participation \n" +
        "left outer join participation.user as user\n " +
        "left outer join participation.activity as activity \n" +
        "GROUP BY user.firstName, user.lastName\n" +
        "ORDER BY SUM(activity.points) DESC" )
    List<ScoreboardAnswer> getScore();
}
