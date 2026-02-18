package org.docksidestage.handson.exercise;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.dbflute.exception.NonSpecifiedColumnAccessException;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.handson.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.handson.dbflute.exentity.*;
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
        
        // done hase プログラムの世界でDayだと、日数とか日だけを指すニュアンスになることが多い by jflute (2025/11/28)
        // ここでは日付、年月日なので、Dateの方が誤解が少ない。renameお願いします。
        LocalDate targetDate = toLocalDate("1968-01-01");

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            // cb.setupSelect_MemberStatus();
            cb.query().setMemberName_LikeSearch(prefix, op -> op.likePrefix());
            // #1on1: 日付の言葉の曖昧さについて (2025/11/28)
            // done hase LocalDateをもっかいLocalDateにしている by jflute (2025/11/28)
            cb.query().setBirthdate_LessEqual(targetDate);
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
//        for (Member member : memberList) {
//            // done hase 変数抽出を by jflute (2025/11/28)
//            log(member.getMemberName() + ", " + member.getBirthdate());
//            // done hase ここ、getMemberStatus(), アサートが成り立ってないです。 by jflute (2025/11/28)
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
        // done hase [読み物課題] コードにコメント書くのにDBにコメント書かないの？ by jflute (2025/11/28)
        // https://jflute.hatenadiary.jp/entry/20170628/letsdbcomment
    }
    // done jflute 次回1on1ここから (2025/11/28)

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
        // done hase ということで修行++がんばってみてください by jflute (2025/12/16)
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
        // #1on1 このベタ書きを、Streamが代理してやってくれているだけ (抽象度が一個上がった) (2026/01/06)
        //Map<Integer, MemberSecurity> manualMap = new HashMap<>();
        //for (MemberSecurity security : securityList) {
        //    manualMap.put(security.getMemberId(), security);
        //}
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

        // done hase Set を使うとさらに良い、パフォーマンス的に by jflute (2026/01/06)
        // List, Set, Map があって、List と Set は似ててちょい違う。
        // List: 重複を許す
        // Set: 重複を許さない => add()してもエラーにならず、ただ吸収されるだけ(追加されない)
        // 今のstatusListは、Aが何回来たか？の情報まで持ってるけど、その必要がない。
        // e.g. A,A,A,A,A,A,A,A,A,A,A,B,B,B,B,B,B,B,B,B,B,B,B,C,C,C,C,C,C,C,C
        // これがSetであれば、e.g. A, B, C
        // ということで、containsのパフォーマンスに影響する。(厳密にはadd()も)
        // Listでやり切るのであれば、add()を最低限にした方が良い by はせ
        // #1on1 HashSetのソースコードも読んでみた
        // #1on1 contains()のソースコードも読んでみた

//        // Listでやるなら、add()を減らす
//        List<String> statusList = new ArrayList<String>(); // Stringは省略可能らしいが勉強用にあえて残しておく by hase (2026/1/2)
//        String previous = null;
//        int alreadyAppearedCount = 0;
//        for (Member member : memberList) {
//            String current = member.getMemberStatusCode();
//            log(current, member.getMemberId());
//            if (previous != null && !previous.equals(current)) {
//                // #1on1 他のやり方の例 (2026/01/06)
//                // e.g. assertFalse(statusList.contains(current));
//                // e.g. ++switchCount;
//                if (statusList.contains(current)) {
//                    alreadyAppearedCount ++;
//                }
//                statusList.add(current);
//            }
//            log(statusList.size());
//            previous = current;
//        }
//        assertEquals(0,alreadyAppearedCount);

        Set<String> statusSet = new java.util.HashSet<>();
        String previous = null;
        for (Member member : memberList) {
            String current = member.getMemberStatusCode();
            log(current, member.getMemberId());
            if(previous != null && !current.equals(previous)) {
                assertFalse(statusSet.contains(current));
            }
            statusSet.add(current);
            previous = current;
        }
    }
    
    public void test_selectPurchaseWithMemberHasBirthdate() throws Exception {
        // ## Arrange ##

        // ## Act ##
        // #1on1: PURCHASEを基点テーブルにしているのが素晴らしい (2026/01/20)
        // 「要件を正確に解釈する」、意外に難しいもの、でもこれができれば効率良い。
        //  → 手戻りを発生させないこと。
        // 国語大事話。 (2026/01/20)
        // 基点テーブルの話。
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
        // #1on1: 問答無用get()を回避しようとして、結局get()と変わらないパターン (2026/01/20)
        //Member member2 = purchase.getMember().orElseThrow(() -> {
        //    throw new IllegalStateException("なかったです。");
        //});
        // 一方で、ちゃんとしてorElseThrow()を書けるか？書こうとするか？
        // なので、DBFluteのOptionalは、組み込みの例外メッセージをリッチにデバッグしやすいようにした。
        // section2の方でも続きの話をした。
        /* ifPresent()の話 (2026/01/20)
        purchase.getMember().ifPresent(mb -> {
            log(mb.getMemberId());
        }).orElse(() -> {
            log("not found");
        });
        purchase.getMember().ifPresentOrElse(mb -> {
            log(mb.getMemberId());
        }, () -> {
            log("not found");
        });
        */
        // #1on1: Optionalのコンセプトは奥深いものなので、コンセプト理解して使わないと...
        // Optionalの意義をなくす実装 (だいなし) をしてしまう可能性がある。 (2026/01/20)
    }

    public void test_selectMemberFormalizedFrom20051001To20051003() throws Exception {
        // ## Arrange ##
        // #1on1: ザ・まで検索、自然言語だと、「まで」は、その日(その瞬間)も含むことが多い。 (2026/02/03)
        // 一方で、"来週の水曜まで休みます" 話。
        String fromDateStr = "2005/10/01";
        String toDateStr = "2005/10/03";
        LocalDateTime fromDate = toLocalDateTime(fromDateStr);
        // （勉強用メモ）↑ これは、PlainTestCaseのメソッドで変換だけ。
        // DBfluteのnew HandyDate(fromDateStr).getLocalDateTime();を使えば、addDay(1)とかも使えて拡張性高い。
        LocalDateTime toDate = toLocalDateTime(toDateStr);
        String wordContainedInName = "vi";
        adjustMember_FormalizedDatetime_FirstOnly(fromDate, wordContainedInName);

        // ## Act ##
        // #1on1: FromToのコンセプトの話。ハンズオンの重要性の話も。 (2026/02/03)
        // #1on1: SpecifyColumn, 業務でやってた経験があるとのこと。 (2026/02/03)
        // DBFluteのデフォルトコンセプトでは、カラムの絞りは任意で、そこまで主張してない。(現場判断に任せる)
        // 現場に寄っては、完全に必須にしてるところもある。必須オプションもある(dfpropにて)。
        // あと、個別に必須にするメソッドもある: cb.enableSpecifyColumnRequired();
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().specifyMemberStatus().columnMemberStatusName();
            cb.query().setFormalizedDatetime_FromTo(fromDate, toDate, op -> op.compareAsDate());
            cb.query().setMemberName_LikeSearch(wordContainedInName, op -> op.likeContain());
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        LocalDateTime toDatePlusOne = toDate.plusDays(1);
        memberList.forEach(member -> {
            MemberStatus status = member.getMemberStatus().get();
            // done hase 主役のカラムは、変数抽出して見やすくしましょう getFormalizedDatetime() by jflute (2026/02/03)
            LocalDateTime formalizedDatetime = member.getFormalizedDatetime();

            String memberName = member.getMemberName();
            String memberStatusName = status.getMemberStatusName();

            log(memberName, formalizedDatetime, memberStatusName);
            // done hase 自分で見つけてくれた。assertFalse()でいい by jflute (2026/02/03)
            // done hase toDate.plusDays(1), ループの中でループ回数分実行されるので、超若干コストが掛かっている by jflute (2026/02/03)
            // #1on1: UnitTestなので目くじらを立てなくてもいいかもだけど、そういった感覚を得てもらうために抽出しましょう。
            // ループの中の処理というのは、繰り返されるので、コストが倍々になっていく意識を持って欲しい。
            // DBアクセスのループも避けたいし、リモートAPIのループも避けたい。
            // done hase [読み物課題] 単純な話、getであんまり検索したくない by jflute (2026/02/03)
            // https://jflute.hatenadiary.jp/entry/20151020/stopgetselect
            assertFalse(formalizedDatetime.isBefore(fromDate));
            assertFalse(formalizedDatetime.isAfter(toDatePlusOne));
            assertTrue(memberName.contains(wordContainedInName));

            assertNotNull(member.getMemberStatusCode());
            assertNotNull(memberStatusName);
            assertException(NonSpecifiedColumnAccessException.class, () -> status.getDisplayOrder()); // specified propertyの管理がどのように行われているか、コードで追えなかったので聞いてみたい
            assertException(NonSpecifiedColumnAccessException.class, () -> status.getDescription());
        });
    }

    // done jflute 次回1on1ここから (2026/02/03)
    public void test_selectPurchaseInOneWeekFromFormalized() throws Exception {
        // ## Arrange ##
        adjustPurchase_PurchaseDatetime_fromFormalizedDatetimeInWeek();
        // moveToDayTerminal()のせいか！！！
        // #1on1: 再びソースコードリーディングのコツ::漠然読みで構造把握して、当たりを付けて(時に逆読み)
        // ログのリーディングでも繋がる話。
        // 漠然読み大事。
        
        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberStatus();
            cb.setupSelect_Member().withMemberSecurityAsOne();
            cb.setupSelect_Product().withProductStatus();
            cb.setupSelect_Product().withProductCategory().withProductCategorySelf();

            // #1on1: 一週間以内という言葉の曖昧さ、両方のパターン (2026/02/18)
            // 10/3                    10/10     10/11
            //  13h                      0h  13h   0h
            //   |                       |    |    |
            //   |       D               | I  |    | P
            // A |                       |H  J|L   |O
            //   |C                  E   G    K    N
            //   B                      F|    |   M|
            //   |                       |         |
            //
            // TODO hase adjustのデータがヒットするように by jflute (2026/02/18)
            // TODO hase fromの方を上に持ってきましょう by jflute (2026/02/18)
            cb.columnQuery(leftCb ->
                    leftCb.specify().columnPurchaseDatetime()
            ).lessEqual(rightCb ->
                    rightCb.specify().specifyMember().columnFormalizedDatetime()
                            .convert(op -> op.addDay(7))
            );
            cb.columnQuery(leftCb ->
                    leftCb.specify().columnPurchaseDatetime()
            ).greaterEqual(rightCb -> // 正式登録前も購入できるの知らなかった、log出して気づいた
                    rightCb.specify().specifyMember().columnFormalizedDatetime()
            );
        });
    
        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            Member member = purchase.getMember().get();
            Product product = purchase.getProduct().get();
            MemberStatus memberStatus = member.getMemberStatus().get();
            MemberSecurity memberSecurity = member.getMemberSecurityAsOne().get();
            ProductStatus productStatus = product.getProductStatus().get();
            ProductCategory productCategory = product.getProductCategory().get();
            ProductCategory parentCategory = productCategory.getProductCategorySelf().get();

            log(member.getMemberName(), memberStatus.getMemberStatusName(), memberSecurity.getReminderQuestion());
            log(product.getProductName(), productStatus.getProductStatusName(), productCategory.getProductCategoryName(),
                    parentCategory.getProductCategoryName());
            // TODO hase 主役級のカラムは変数に抽出して、大事なロジックのところを見やすくしよう by jflute (2026/02/18)
            log(purchase.getPurchaseDatetime(), member.getFormalizedDatetime());

            assertFalse(purchase.getPurchaseDatetime().isAfter(member.getFormalizedDatetime().plusDays(7)));
            assertFalse(purchase.getPurchaseDatetime().isBefore(member.getFormalizedDatetime()));
        });
    }
    
    public void test_selectMemberWithBirthdateBeforeEqual1974OrNull() throws Exception {
        // ## Arrange ##
        String targetDateStr = "1974/01/01";
        LocalDate targetDate = toLocalDate(targetDateStr);

        String okDateStr = "1974/12/31";
        String ngDateStr = "1975/01/01";
        LocalDate limitDate = toLocalDate(okDateStr);
        LocalDate ngDate = toLocalDate(ngDateStr);
        // TODO hase adjustメソッド作る？ by hase (2026/02/17)

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.setupSelect_MemberSecurityAsOne();
            cb.setupSelect_MemberWithdrawalAsOne();
            // #1on1: orScopeQuery()は汎用的なor, 一方で、FromToのorIsNull()はピンポイントのor
            //cb.orScopeQuery(orCB -> {
            //    orCB.query().setBirthdate_FromTo(null, targetDate, op -> op.compareAsYear());
            //    orCB.query().setBirthdate_IsNull();
            //});
            cb.query().setBirthdate_FromTo(null, targetDate, op -> op.compareAsYear().orIsNull());
            cb.query().addOrderBy_Birthdate_Desc().withNullsFirst();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        boolean limitDateAppeared = false;
        for (Member member : memberList) {
            MemberStatus status = member.getMemberStatus().get();
            MemberSecurity security = member.getMemberSecurityAsOne().get();
            log(status.getMemberStatusName(), security.getReminderQuestion(), security.getReminderAnswer(),
                    member.getMemberWithdrawalAsOne().map(w -> w.getWithdrawalReasonInputText()).orElse("none"));

            LocalDate birthdate = member.getBirthdate();
            if (birthdate != null) {
                assertTrue(birthdate.isBefore(ngDate));
            }
            if (birthdate == limitDate) {
                log("border line asserted correctly!!!" + member.getMemberName(), birthdate);
                limitDateAppeared = true;
            }
        }
//        assertTrue(limitDateAppeared);
        assertNull(memberList.get(0).getBirthdate());
    }
}
