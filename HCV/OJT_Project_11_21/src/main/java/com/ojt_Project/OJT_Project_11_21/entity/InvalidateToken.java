package com.ojt_Project.OJT_Project_11_21.entity;


import java.util.Collection;
import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class InvalidateToken {
 @Id
 String id;
 Date expiryTime;
}
