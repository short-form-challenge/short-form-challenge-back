package com.leonduri.d7back.api.challenge;

import com.leonduri.d7back.api.user.User;
import com.leonduri.d7back.api.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "day_cnt", columnDefinition = "Int default 0")
    private Integer dayCnt;

    @Column(name = "badge_cnt", columnDefinition = "Int default 0")
    private Integer badgeCnt;

    @Column(name = "last_challenged_at")
    private LocalDate lastChallengedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void setDayCntZero() {
        this.dayCnt = 0;
    }

    public void increaseDayCnt() {
        this.dayCnt += 1;
    }

    public void increaseBadgeCnt() {
        this.badgeCnt += 1;
    }

    public Challenge(User user, Category category) {
        this.badgeCnt = 0;
        this.dayCnt = 1;
        this.lastChallengedAt = LocalDate.now();
        this.user = user;
        this.category = category;
    }

    public void update(boolean todayDone) {
        if (this.lastChallengedAt.equals(LocalDate.now())) return; // 하루에 2번 이상 올림

        boolean yesterdayDone = (this.lastChallengedAt.equals(LocalDate.now().minusDays(1L)));
        if(todayDone) this.lastChallengedAt = LocalDate.now();

        //오늘도 하고 어제도 함
        if(todayDone && yesterdayDone){
            this.dayCnt++;
            if(this.dayCnt >= 7){
                this.badgeCnt += this.dayCnt / 7;
                this.dayCnt %= 7;
            }
        }
        // 오늘은 했는데 어제는 안함
        else if(todayDone && !yesterdayDone){
            this.dayCnt = 1;
        }
        // 어제 안함
        else if (!yesterdayDone) {
            this.dayCnt = 0;
        }
    }
}
