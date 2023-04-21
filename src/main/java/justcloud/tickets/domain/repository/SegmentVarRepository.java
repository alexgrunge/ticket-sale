package justcloud.tickets.domain.repository;

import java.util.List;
import justcloud.tickets.domain.tickets.SegmentVar;
import justcloud.tickets.domain.tickets.ServiceLevelType;
import justcloud.tickets.domain.tickets.StopOff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface SegmentVarRepository extends PagingAndSortingRepository<SegmentVar, String> {
  @Query("select s from SegmentVar s where startingStop in :startingStops")
  List<SegmentVar> findByStartingStops(@Param("startingStops") List<StopOff> startingStops);

  SegmentVar findByStartingStopIdAndEndingStopIdAndCategoryAndServiceLevelType(
      String startingId, String endingId, String category, ServiceLevelType serviceLevelType);
}
