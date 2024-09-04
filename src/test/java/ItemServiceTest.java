import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.ItemServiceImpl;
import ru.practicum.item.UrlMetaDataRetrieverImpl;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ItemServiceTest {
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UrlMetaDataRetrieverImpl urlMetaDataRetriever = Mockito.mock(UrlMetaDataRetrieverImpl.class);
    private ItemServiceImpl itemService = new ItemServiceImpl(itemRepository, userRepository, urlMetaDataRetriever);

    @Test
    public void changeItemTest(){
        Set<String> newtags = Set.of("tag3","tag4");
        ModifyItemRequest request = ModifyItemRequest.of(1L, true,newtags,false);

        String url = "http://url.ru";
        Item item = new Item();
        item.setId(1L);
        item.setUrl(url);
        item.setDateResolved(Instant.now());

        Set<String> tags = Set.of("tag1","tag2");
        item.setTags(tags);

        User user = new User();
        user.setId(1L);

        item.setUser(user);

        when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
        when(itemRepository.save(Mockito.any())).thenReturn(item);

        itemService.changeItem(1L, request);

        verify(itemRepository, times(1)).findById(Mockito.any());
        verify(itemRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void deleteItemTest(){
        doNothing().when(itemRepository).deleteByUserIdAndId(1L, 1L);

        itemService.deleteItem(1L, 1L);

        verify(itemRepository, times(1)).deleteByUserIdAndId(1L, 1L);
    }
}
