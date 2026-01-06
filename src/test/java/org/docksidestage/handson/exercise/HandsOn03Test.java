package org.docksidestage.handson.exercise;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.handson.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.dbflute.exentity.MemberSecurity;
import org.docksidestage.handson.dbflute.exentity.Product;
import org.docksidestage.handson.dbflute.exentity.Purchase;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// #1on1: 最近、調子が悪い (2025/12/16)
// AIに書かせるので自分で書けないのは良いとして、現時点では読めないといけない。
// 読むためにはバイブコーディングから学びを得ないといけない。コードを出してOKで済ませてはいけない。
// また、そのフレームワーク自身のコンセプトから理解をするっていうのやらないと、
// バイブコーディングで90点のコードは出せても、残り10点を埋める作業が人間ができない。
//
// 積み上げはできる時代だけど、積み上げたものの抜けを見つけたり修正するのは根本のスキルが必要。
// でもこれはAIじゃなくてもある程度は同じで、昔も付け焼き刃の知識をぶち込んで足元ぐらぐらな人はたくさんいた。
// なので、エッセンシャル(根本を問うよう)なトレーニングをやる機会を増やす。
// バイブコーディングでも悪くないと思うけど、それを意識しながらやらないと、学びならない。
//
// 量が質になる話。
// 逆に、一気に量を増やすってのができないなら、焦らなくて良い。(できなくてもいいという面もあるし)
//
// AI時代のロジカルシンキングの話。

/**
 * セクション3のテストクラス
 * <ul>
 *     <li>会員名称がSで始まる1968年1月1日以前に生まれた会員を検索</li>
 *     <li>会員ステータスと会員セキュリティ情報も取得して会員を検索</li>
 *     <li>会員セキュリティ情報のリマインダ質問で2という文字が含まれている会員を検索</li>
 * </ul>
 * <ul>
 *     <li>会員ステータスの表示順カラムで会員を並べて検索</li>
 *     <li>生年月日が存在する会員の購入を検索</li>
 *     <li>2005年10月の1日から3日までに正式会員になった会員を検索</li>
 * </ul>
 *
 * @author hase
 */
public class HandsOn03Test extends UnitContainerTestCase {

    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private PurchaseBhv purchaseBhv;

    public void test_selectMemberStartsWithSBornBefore1968() throws Exception {
        // ## Arrange ##
        String prefix = "S";
        // TODO done hase プログラムの世界でDayだと、日数とか日だけを指すニュアンスになることが多い by jflute (2025/11/28)
       // ここでは日付、年月日なので、Dateの方が誤解が少ない。renameお願いします。
        LocalDate targetDate = toLocalDate("1968-01-01");

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            // cb.setupSelect_MemberStatus();
            cb.query().setMemberName_LikeSearch(prefix, op -> op.likePrefix());
            // #1on1: 日付の言葉の曖昧さについて (2025/11/28)
            // TODO done hase LocalDateをもっかいLocalDateにしている by jflute (2025/11/28)
            cb.query().setBirthdate_LessEqual(targetDate);
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
//        for (Member member : memberList) {
//            // TODO done hase 変数抽出を by jflute (2025/11/28)
//            log(member.getMemberName() + ", " + member.getBirthdate());
//            // TODO done hase ここ、getMemberStatus(), アサートが成り立ってないです。 by jflute (2025/11/28)
//            // setupSelectを一時的にコメントアウトしても落ちないのはなぜ？
        // fk制約でnot nullだから、必ず存在する。
//            assertNotNull(member.getMemberStatus());
//            assertTrue(member.getMemberName().startsWith(prefix));
//            assertFalse(member.getBirthdate().isAfter(toLocalDate(targetDate)));
//        }
        memberList.forEach(member -> {
            String memberName = member.getMemberName();
            LocalDate birthdate = member.getBirthdate();
            log(memberName + ", " + birthdate);
            assertTrue(memberName.startsWith(prefix));
            assertFalse(birthdate.isAfter(targetDate));
        });
    }

    public void test_selectMemberWithStatusAndSecurity() throws Exception {
        // ## Arrange ##
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.setupSelect_MemberSecurityAsOne();
            cb.query().addOrderBy_Birthdate_Desc();
            cb.query().addOrderBy_MemberId_Asc();
        });
    
        // ## Assert ##
        // #1on1: さて、絞り込みもなくすべての会員を取っていますが...
        // 会員ステータスは、すべての会員で存在するといい切れるのでしょうか？
        // 会員セキュリティは、すべての会員で存在するといい切れるのでしょうか？
        // (いまの実装は、そのようになっていますが、それを裏付けるものは何？)
        //
        // 会員ステータス:
        //  → not nullでFK制約が貼られているから、探しにいけば絶対にあるといいきれる
        // 会員セキュリティ:
        //  → 物理的な制約はないけど、業務的にはERDの黒丸で必ず存在することになってる
        //  → あと、DBコメントに「会員一人につき必ず一つのセキュリティ情報がある」と書いてある
        //
        // 会員から会員ステータスは、NotNullのFKカラムで参照しているので、探しにいけば必ず存在する
        // 会員から会員セキュリティは、FKの方向と探しにいく方向が逆なので同じ理論にはなりませんが、
        // ERDのリレーションシップ線に注目。会員退会情報と比べると一目瞭然、黒丸がついていないので必ず存在する1
        //   会員から会員セキュリティ => 1:必ず1 (1:1)
        //   会員から会員退会情報    => 1:いないかもしれない1 (1:0..1)
        // ただ、物理的な制約はありません。業務的というのは、そういうルールにしているいうことだけなんですね。
        // 細かいですが、これがデータベースプログラミングにおいて、とても重要なんですよね。
        // ぜひ、カージナリティに着目してみてください。
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            assertTrue(member.getMemberStatus().isPresent());
            assertTrue(member.getMemberSecurityAsOne().isPresent());
        });
        // TODO done hase [読み物課題] コードにコメント書くのにDBにコメント書かないの？ by jflute (2025/11/28)
        // https://jflute.hatenadiary.jp/entry/20170628/letsdbcomment
    }
    // TODO jflute 次回1on1ここから (2025/11/28)

    public void test_selectMemberWithSecurityQuestionContaining2() throws Exception {
        // ## Arrange ##
        String wordIncluded = "2";

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            // "会員セキュリティ情報のデータ自体は要らない" なのでこれはやってはいけない by jflute
            //cb.setupSelect_MemberSecurityAsOne();
            cb.query().queryMemberSecurityAsOne().setReminderQuestion_LikeSearch(wordIncluded, op -> op.likeContain());
        });

        // ## Assert ##
        // #1on1: できたけど、別のやり方と比較もできないので理解がまだしっくり来ていない (2025/12/16)
        // 修行++を置いておくと、実装としてはパーフェクトです。
        // memberSecurity じゃなくて security という短い名前もGood (スコープ短いし明らかだし)
        // (question も同じく)
        //
        // #1on1: ロジックを考えていく進めていくのが気が進まないときは...
        // ハンズオンでは、ショートカットキーを覚えるとか、そういうのに時間使っても良い。
        // (メインのことがどうしても気が進まないのであれば、サイドのことをやるでも悪くない)
        //
        // #1on1: 修行++の話。テストだからループ内で検索でもOKだけど、メインコードだったら基本的に非推奨。
        // SQLの発行するという事務的な行為自体が20倍になってパフォーマンス少し落ちるということがある。
        // TODO done hase ということで修行++がんばってみてください by jflute (2025/12/16)
        assertHasAnyElement(memberList);
//        memberList.forEach(member -> {
//            memberSecurityBhv.selectByPK(member.getMemberId()).alwaysPresent(security -> {
//                String question = security.getReminderQuestion();
//                log(member.getMemberName(), question);
//                assertTrue(question.contains(wordIncluded));
//            });
//        });
        ListResultBean<MemberSecurity> securityList = memberSecurityBhv.selectList(cb -> {
            cb.query().setMemberId_InScope(memberBhv.extractMemberIdList(memberList));
        });
        Map<Integer, MemberSecurity> securityMap = securityList.stream()
                .collect(Collectors.toMap(security -> security.getMemberId(), security -> security));
        memberList.forEach(member -> {
            MemberSecurity security = securityMap.get(member.getMemberId());
            String question = security.getReminderQuestion();
            log(member.getMemberName(), question);
            assertTrue(question.contains(wordIncluded));
        });
        /*
  o
 /｜\ -+                  +------------------- Server World -----------------------------+
  /\   |                  |                                                              |
       |                  |          +-----------------+                      /----\     |
 +-----------+            |          |      Java       |        (SQL)        /      \    |
 | Browser   |            |          |                 | ------------------> |  DB   |   |
 |           |            |          |                 |                     |       |   |
 |  HTML     |            |          |                 |     JDBC            |       |   |
 |   Control |            |          |                 |                     |       |   |
 |    |      |            |          |                 |                     |       |   |
 |    v      |    (HTTP request)     |                 |                     |       |   |
 |  Data     | --------------------> |                 |                     |       |   |
 |   Control |                       |                 |                     |       |   |
 |           | <-------------------- | (HTML Template) | <------------------ |       |   |
 +-----------+    (HTTP response)    +-----------------+        (data)       +-------+   |
 JavaScript               |                   ^    ^                                     |
 (HTML Template)          |                   |    |                                     |
      ^                   +------------------/T\--/T\------------------------------------+ 
      |                                       |    |
      |  (request)                            |    |
      |-------Ajax----------------------------+    |
        (JSON response)                            |
                                                   |
 +--------+    (request)                           |
 | iPhone | <--------------------------------------+
 +--------+  (JSON response) e.g. {"name": "jflute", "favorite": "sea"}
         */
    }

    public void test_selectMemberOrderByStatusDisplayOrder() throws Exception {
        // ## Arrange ##
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().queryMemberStatus().addOrderBy_DisplayOrder_Asc();
            cb.query().addOrderBy_MemberId_Desc();
        });
        // ## Assert ##
        assertHasAnyElement(memberList);

        memberList.forEach(member -> {
            assertFalse(member.getMemberStatus().isPresent());
        });

        List<String> statusList = new ArrayList<String>(); // Stringは省略可能らしいが勉強用にあえて残しておく by hase (2026/1/2)
        String previous = null;
        int alreadyAppearedCount = 0;
        for (Member member : memberList) {
            String current = member.getMemberStatusCode();
            log(current, member.getMemberId());
            if (previous != null && !previous.equals(current)) {
                if (statusList.contains(current)) {
                    alreadyAppearedCount ++;
                }
            }
            statusList.add(current);
            previous = current;
        }
        assertEquals(0,alreadyAppearedCount);
    }
    
    public void test_selectPurchaseWithMemberHasBirthdate() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberStatus();
            cb.setupSelect_Product();
            cb.query().queryMember().setBirthdate_IsNotNull();
            cb.query().addOrderBy_PurchaseDatetime_Desc();
            cb.query().addOrderBy_PurchasePrice_Desc();
            cb.query().addOrderBy_ProductId_Asc();
            cb.query().addOrderBy_MemberId_Asc();
        });
    
        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            purchase.getMember().alwaysPresent(member -> {
                Product product = purchase.getProduct().get();
                log(member.getMemberName(), member.getMemberStatus().get(), product.getProductName());
                log(purchase.getPurchaseDatetime(), purchase.getPurchasePrice(), product.getProductId(), member.getMemberId());
                assertNotNull(member.getBirthdate());
            });
        });
    }

    public void test_selectMemberRegisteredFrom20051001To20051003() throws Exception {
        // ## Arrange ##

        LocalDate fromDate = toLocalDate("2005-10-01");  // 日付だけ
        LocalDate toDate = toLocalDate("2005-10-03");    // 日付だけ
        String wordContainedInName = "vi";

        // ## Act ##

        // ## Assert ##
    }
}
