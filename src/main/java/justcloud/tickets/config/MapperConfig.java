package justcloud.tickets.config;

import justcloud.tickets.domain.*;
import justcloud.tickets.domain.sales.ClientAccount;
import justcloud.tickets.domain.sales.PackageTicket;
import justcloud.tickets.domain.sales.SalesTerminal;
import justcloud.tickets.domain.tickets.*;
import justcloud.tickets.domain.tickets.TripExpense;
import justcloud.tickets.dto.*;
import justcloud.tickets.search.Location;
import justcloud.tickets.search.RouteSegment;
import justcloud.tickets.search.RouteSegment.StopData;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Configuration
public class MapperConfig {

  @Bean
  public MapperFactory mapperFactory() {
    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    factory
        .classMap(Scale.class, Location.class)
        .field("latitude", "lat")
        .field("longitude", "lon")
        .register();

    factory
        .classMap(TripExpense.class, justcloud.tickets.dto.TripExpense.class)
        .byDefault()
        .customize(
            new CustomMapper<TripExpense, justcloud.tickets.dto.TripExpense>() {
              @Override
              public void mapAtoB(
                  TripExpense tripExpense,
                  justcloud.tickets.dto.TripExpense tripExpense2,
                  MappingContext context) {
                tripExpense2.setReceipt(
                    tripExpense.getExpenseType() == TripExpense.ExpenseType.WITH_RECEIPT);
              }
            })
        .register();

    factory.classMap(ServiceLevelType.class, ServiceTypeData.class).byDefault().register();

    factory.classMap(ServiceType.class, ServiceTypeData.class).byDefault().register();

    factory
        .classMap(TripSeat.class, TicketData.class)
        .field("seat", "position")
        .byDefault()
        .register();

    factory.classMap(EmployeeLoan.class, EmployeeLoanData.class).byDefault().register();

    factory.classMap(EmployeeDiscount.class, EmployeeDiscountData.class).byDefault().register();

    factory.classMap(EmployeeAccount.class, EmployeeAccountData.class).byDefault().register();

    factory.classMap(Employee.class, EmployeeData.class).byDefault().register();

    factory.classMap(Individual.class, EmployeeData.class).byDefault().register();

    factory
        .classMap(Bus.class, BusData.class)
        .byDefault()
        .customize(
            new CustomMapper<Bus, BusData>() {

              @Override
              public void mapAtoB(Bus a, BusData b, MappingContext context) {
                if (a != null && a.getStatus() != null) {
                  b.setStatus(a.getStatus().toString());
                }
              }
            })
        .register();

    factory.classMap(PackageTicket.class, BaggageData.class).byDefault().register();

    factory.classMap(User.class, UserData.class).byDefault().register();

    factory.classMap(ClientAccount.class, AccountData.class).byDefault().register();

    factory
        .classMap(SalesTerminal.class, SalesTerminalData.class)
        .field("officeLocation.name", "officeName")
        .byDefault()
        .register();

    factory
        .classMap(SalesTerminal.class, SalesBoothData.class)
        .field("officeLocation.name", "officeName")
        .byDefault()
        .register();

    factory
        .classMap(StopOff.class, RouteSegment.StopData.class)
        .field("scale", "location")
        .byDefault()
        .customize(
            new CustomMapper<StopOff, RouteSegment.StopData>() {

              @Override
              public void mapAtoB(StopOff a, StopData b, MappingContext context) {
                String[] input = new String[] {a.getName()};
                Completion suggestion = new Completion(input);
                suggestion.setOutput(a.getName());
                b.setSuggestion(suggestion);
              }
            })
        .register();

    return factory;
  }

  @Bean
  public MapperFacade mapper() {
    return mapperFactory().getMapperFacade();
  }
}
