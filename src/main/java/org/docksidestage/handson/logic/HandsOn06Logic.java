package org.docksidestage.handson.logic;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// #1on1: ログの設定、一度は目にしておこう (2026/06/30)
// #1on1: 和名の設定 (2026/06/30)
// #1on1: 大文字小文字の設定 (2026/06/30)
// #1on1: 空文字入れない設定 (2026/06/30)
// #1on1: DBFluteアップグレード、DBFlute Client, DBFlute Engine, DBFlute Runtime (2026/06/30)
// #1on1: decommentのお話 (2026/06/30)
// #1on1: SchemaPolicyCheckのお話、DB設計の統一性の担保 (2026/06/30)
public class HandsOn06Logic {

    private static final Logger logger = LoggerFactory.getLogger(HandsOn06Logic.class);

    @Resource
    private MemberBhv memberBhv;

    public ListResultBean<Member> selectSuffixMemberList(String suffix) {
        if (suffix == null || suffix.trim().isEmpty()) {
            throw new IllegalArgumentException("The suffix should not be null or empty: " + suffix);
        }
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setMemberName_LikeSearch(suffix, op -> op.likeSuffix());
            cb.query().addOrderBy_MemberName_Asc();
        });
        memberList.forEach(member -> {
            logger.debug("memberName={}, birthdate={}, formalizedDatetime={}",
                    member.getMemberName(), member.getBirthdate(), member.getFormalizedDatetime());
        });
        return memberList;
    }
}
