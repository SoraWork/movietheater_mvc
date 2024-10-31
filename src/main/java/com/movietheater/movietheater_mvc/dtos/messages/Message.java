package com.movietheater.movietheater_mvc.dtos.messages;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String type;
    private String content;
}
