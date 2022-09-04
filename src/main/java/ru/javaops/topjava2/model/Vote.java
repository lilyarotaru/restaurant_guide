package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(name = "uk_vote_date_user", columnNames = {"vote_date", "user_id"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    public static final LocalTime DEADLINE = LocalTime.of(11, 0);

    @Column(name = "vote_date", columnDefinition = "date default now()")
    @NotNull
    private LocalDate voteDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    public Vote(LocalDate voteDate, User user, Integer restaurantId){
        this.voteDate=voteDate;
        this.user=user;
        this.restaurantId = restaurantId;
    }
}
