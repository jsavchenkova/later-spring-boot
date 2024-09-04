import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.dto.ModifyItemRequest;
import ru.practicum.item.model.Item;
import ru.practicum.note.ItemNote;
import ru.practicum.note.ItemNoteDto;
import ru.practicum.note.ItemNoteRepository;
import ru.practicum.note.ItemNoteServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemNoteSrviceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemNoteRepository itemNoteRepository;
    private ItemNoteServiceImpl itemNoteService = new ItemNoteServiceImpl(itemNoteRepository
            , itemRepository);

    @Test
    public void addNewItemNote() {
        ItemNoteDto itemNoteDto = ItemNoteDto.builder()
                .dateOfNote(LocalDate.now().toString())
                .itemId(1L)
                .itemUrl("http://url.ru")
                .text("text")
                .build();

        Item item = new Item();
        item.setId(1L);
        item.setUrl("http://url.ru");

        ItemNote itemNote = new ItemNote();
        itemNote.setItem(item);
        itemNote.setDateOfNote(Instant.now());
        itemNote.setText("text");

        when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
        when(itemNoteRepository.save(Mockito.any())).thenReturn(itemNote);

        itemNoteService.addNewItemNote(1L, itemNoteDto);

        Mockito.verify(itemRepository, times(1)).findById(Mockito.any());
        Mockito.verify(itemNoteRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void searchNotesByUrlTest(){
        String url = "http://url.ru";
        ItemNoteDto itemNoteDto = ItemNoteDto.builder()
                .dateOfNote(LocalDate.now().toString())
                .itemId(1L)
                .itemUrl(url)
                .text("text")
                .build();

        Item item = new Item();
        item.setId(1L);
        item.setUrl(url);

        ItemNote itemNote = new ItemNote();
        itemNote.setItem(item);
        itemNote.setDateOfNote(Instant.now());
        itemNote.setText("text");

        when(itemNoteRepository.findAllByItemUrlContainingAndItemUserId(Mockito.any(), Mockito.any())).
                thenReturn(List.of(itemNote));

        List<ItemNoteDto> result = itemNoteService.searchNotesByUrl(url, 1L);

        assertThat(1).isEqualTo(result.size());
        assertThat(itemNoteDto.getItemUrl()).isEqualTo(result.stream().findFirst().get().getItemUrl());

    }

    @Test
    public void searchNotesByTagTest(){
        String url = "http://url.ru";
        ItemNoteDto itemNoteDto = ItemNoteDto.builder()
                .dateOfNote(LocalDate.now().toString())
                .itemId(1L)
                .itemUrl(url)
                .text("text")
                .build();

        Item item = new Item();
        item.setId(1L);
        item.setUrl(url);

        Set<String> tags = Set.of("tag1","tag2");
        item.setTags(tags);

        ItemNote itemNote = new ItemNote();
        itemNote.setItem(item);
        itemNote.setDateOfNote(Instant.now());
        itemNote.setText("text");

        when(itemNoteRepository.findByTag(Mockito.any(), Mockito.any())).
                thenReturn(List.of(itemNote));

        List<ItemNoteDto> result = itemNoteService.searchNotesByTag(1L, "tag2");

        verify(itemNoteRepository, times(1)).findByTag(Mockito.any(), Mockito.any());
        assertThat(1).isEqualTo(result.size());
        assertThat(itemNoteDto.getItemUrl()).isEqualTo(result.stream().findFirst().get().getItemUrl());

    }


}
