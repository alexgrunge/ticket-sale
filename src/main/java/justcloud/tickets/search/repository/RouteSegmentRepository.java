package justcloud.tickets.search.repository;

import justcloud.tickets.search.RouteSegment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RouteSegmentRepository extends ElasticsearchRepository<RouteSegment, String> {

  @Query(
      "{\"bool\": {\"must\": [{\"term\": {\"startingStop.name.raw\": \"?0\"}},{\"term\":{\"endingStop.name.raw\":\"?1\"}}]}}")
  public Page<RouteSegment> findByStartingStopNameAndEndingStopName(
      String startingName, String endingName, Pageable pageable);

  @Query("{\"bool\": {\"must\": [{\"term\": {\"startingStop.name.raw\": \"?0\"}}]}}")
  public Page<RouteSegment> findByStartingStopName(String startingName, Pageable pageable);
}
