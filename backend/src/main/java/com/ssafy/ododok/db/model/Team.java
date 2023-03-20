package com.ssafy.ododok.db.model;

import com.ssafy.ododok.api.request.TeamModifyPatchReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @ColumnDefault("1")
    private Integer teamMemberCnt;

    @Column(nullable = false)
    private int teamMemberCntMax;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Onoff teamOnoff;

    @ColumnDefault("1")
    private boolean teamRecruit;

    @Column(columnDefinition = "LONGTEXT")
    private String teamRecruitText;

    @Column(nullable = false)
    private String teamRegion;

    @Column(nullable = false)
    private String teamGenre1;

    @Column
    private String teamGenre2;

    @Column
    private String teamGenre3;

    public void updateTeam(TeamModifyPatchReq teamModifyPatchReq){

        this.teamMemberCntMax = teamModifyPatchReq.getTeamMemberCntMax();
        this.teamOnoff = teamModifyPatchReq.getTeamOnoff();
        this.teamRegion = teamModifyPatchReq.getTeamRegion();
        this.teamGenre1 = teamModifyPatchReq.getTeamGenre1();
        this.teamGenre2 = teamModifyPatchReq.getTeamGenre2();
        this.teamGenre3 = teamModifyPatchReq.getTeamGenre3();
        this.teamRecruit = teamModifyPatchReq.isTeamRecruit();

    }

}

