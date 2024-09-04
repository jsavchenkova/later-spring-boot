import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.config.AppConfig;
import ru.practicum.config.PersistenceConfig;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemServiceImpl;
import ru.practicum.item.UrlMetaDataRetrieverImpl;
import ru.practicum.item.dto.AddItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.note.ItemNoteDto;
import ru.practicum.note.ItemNoteServiceImpl;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserServiceImpl;
import ru.practicum.user.UserState;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback(false)
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/later",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
               AppConfig.class
                , PersistenceConfig.class
               , ItemNoteServiceImpl.class
        , ItemServiceImpl.class
                ,UserServiceImpl.class
                , UrlMetaDataRetrieverImpl.class
        })
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemNoteServiceImplIntegrationTest {

//    private final EntityManager em;
    private final ItemNoteServiceImpl service;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    void saveTest(){


        User user = new User();
        user.setEmail("email1");
        user.setState(UserState.ACTIVE);
        user.setFirstName("name1");
        user.setLastName("lastName1");
        user.setRegistrationDate(Instant.now());

        userService.saveUser(UserMapper.mapToUserDto(user));

         Item item = new Item();
         item.setUrl("http://url.ru");
         item.setUser(user);
        AddItemRequest itemRequest = new AddItemRequest();
        itemRequest.setUrl("http://url.ru");

         itemService.addNewItem(1L, itemRequest);

        ItemNoteDto itemNoteDto = ItemNoteDto.builder()
                .dateOfNote(LocalDate.now().toString())
                .itemId(1L)
                .itemUrl("http://url.ru")
                .text("text")
                .build();

        ItemNoteDto newItem = service.addNewItemNote(1L, itemNoteDto);

         assertThat(itemNoteDto.getItemId()).isEqualTo(newItem.getItemId());
         assertThat(itemNoteDto.getText()).isEqualTo(newItem.getText());
         assertThat(itemNoteDto.getItemUrl()).isEqualTo(newItem.getItemUrl());

    }


}
