package se.magnus.microservices.composite.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import se.magnus.api.core.product.Product;
import se.magnus.api.event.Event;

import static org.hamcrest.MatcherAssert.assertThat;
import static se.magnus.microservices.composite.product.IsSameEvent.sameEventExceptCreatedAt;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class IsSameEventTests
{
  ObjectMapper mapper = new ObjectMapper();

  @Test
  void testEventObjectCompare() throws JsonProcessingException
  {

    // Event #1 and #2 are the same event, but occurs as different times
    // Event #3 and #4 are different events
    Event<Integer, Product> event1 = new Event<>( Event.Type.CREATE, 1, new Product( 1, "name", 1, null ) );
    Event<Integer, Product> event2 = new Event<>( Event.Type.CREATE, 1, new Product( 1, "name", 1, null ) );
    Event<Integer, Product> event3 = new Event<>( Event.Type.DELETE, 1, null );
    Event<Integer, Product> event4 = new Event<>( Event.Type.CREATE, 1, new Product( 2, "name", 1, null ) );

    String event1Json = mapper.writeValueAsString( event1 );

    assertThat( event1Json, is( sameEventExceptCreatedAt( event2 ) ) );
    assertThat( event1Json, not( sameEventExceptCreatedAt( event3 ) ) );
    assertThat( event1Json, not( sameEventExceptCreatedAt( event4 ) ) );
  }

}
