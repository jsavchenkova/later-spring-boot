package ru.practicum.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemNoteDto {
    private Long id;
    private Long itemId;
    private String text;
    private String dateOfNote;
    private String itemUrl;
}