show user;
-- USER이(가) "LOGITECH"입니다.

select *
from all_constraints;


-- @@ 회원등급 테이블 @@ --
create table membership
(membershipname     varchar2(10) not null
,minstandard        number(10) not null
,maxstandard        number(10) not null
,constraint PK_membership primary key(membershipname)
,constraint UQ_membership_minsd unique(minstandard)
,constraint UQ_membership_maxsd unique(maxstandard)
);
-- Table MEMBERSHIP이(가) 생성되었습니다.


-- @@ 제품 카테고리 테이블 @@ --
create table productcategory
(category   varchar2(10) not null
,constraint PK_productcategory primary key(category)
);
-- Table PRODUCTCATEGORY이(가) 생성되었습니다.


-- @@ 제품 테이블 @@ --
create table product
(productid      varchar2(20) not null
,productname    varchar2(30) not null
,fk_category    varchar2(10) not null
,character      varchar2(100) not null
,price          number(7) not null
,constraint PK_product_productid primary key(productid)
,constraint UQ_product_productname unique(productname)
,constraint FK_product foreign key(fk_category) references productcategory(category)
);
-- Table PRODUCT이(가) 생성되었습니다.

select *
from product;


-- @@ 구매 테이블 @@ --
create table purchase
(purchaseno     number not null
,fk_memberno    number not null
,receiver       varchar2(10) not null
,postcode       varchar2(5) not null
,address        varchar2(200) not null
,detailaddress  varchar2(50) not null
,extraaddress   varchar2(50) not null
,payment        varchar2(10) not null
,totalprice     number(10) not null
,purchaseday    date default sysdate not null
,deliverystatus varchar2(20) default '결제완료' not null
,constraint PK_purchase_purchaseno primary key(purchaseno)
,constraint FK_purchase foreign key(fk_memberno) references member(memberno)
);
-- Table PURCHASE이(가) 생성되었습니다.

create sequence purchase_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence PURCHASE_SEQ이(가) 생성되었습니다.

alter table purchase add totalprice number(10) not null;
alter table purchasedetail drop column price;
alter table purchase drop column deliverystatus;
alter table purchasedetail add deliverystatus varchar2(20) default '결제완료' not null;

alter table purchase

insert into purchase(purchaseno, fk_memberno, receiver, postcode, address, detailaddress, extraaddress, payment, totalprice, purchaseday)
values(purchase_seq.nextval, 10, '김김박', '10579', '경기 고양시 덕양구 오금로 7', '101동 101호', ' (신원동, 신원마을엘에이치3단지)', 'Kakao', 100000, sysdate);

commit;

select *
from purchasedetail;



-- @@ 판매제품 옵션 테이블 @@ --
drop table productoption purge;
create table productoption
(productserialid varchar2(30) not null
,fk_productid   varchar2(20) not null
,color          varchar2(10) not null
,imgfilename    varchar2(4000) not null
,detailimg      varchar2(4000) not null
,carouselimg    varchar2(4000)
,stock          number(5) default 0 not null
,saleday        date
,constraint PK_productoption primary key(productserialid)
,constraint FK_productoption foreign key(fk_productid) references product(productid)
);
-- Table PRODUCTOPTION이(가) 생성되었습니다.

create sequence productoption_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from productoption;


-- @@ 구매상세 테이블 @@ --
create table purchasedetail
(purchasedetailid number not null
,fk_purchaseno number not null
,fk_productserialid varchar2(30) not null
,volume number(5) not null
,constraint PK_purchasedetail primary key(purchasedetailid)
,constraint FK_purchasedetail_pur foreign key(fk_purchaseno) references purchase(purchaseno)
,constraint FK_purchasedetail_pro foreign key(fk_productserialid) references productoption(productserialid)
);
-- Table PURCHASEDETAIL이(가) 생성되었습니다.

create sequence purchasedetail_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence PURCHASEDETAIL_SEQ이(가) 생성되었습니다.

desc purchasedetail;

select *
from purchasedetail;

alter table purchasedetail add expense number(10) not null;

insert into purchasedetail(purchasedetailid, fk_purchaseno, fk_productserialid, volume, expense)
values(purchasedetail_seq.nextval, 2, 'X50_1', 1, 210000);


-- @@ 제품문의 테이블 @@ --
create table productqa
(seq_qa number not null
,fk_memberno number not null
,fk_productid varchar2(20) not null
,content varchar2(100) not null
,writeday date default sysdate not null
,status number(1) default 0 not null
,constraint PK_productqa_seq_qa primary key(seq_qa)
,constraint FK_productqa_fk_memberno foreign key(fk_memberno) references member(memberno)
,constraint FK_productqa_fk_productid foreign key(fk_productid) references product(productid)
);
-- Table PRODUCTQA이(가) 생성되었습니다.

create sequence productqa_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence PRODUCTQA_SEQ이(가) 생성되었습니다.


-- @@ 제품문의답변 테이블 @@ --
create table qaanswer
(fk_seq_qa number not null
,content varchar2(100) not null
,constraint PK_qaanswer primary key(fk_seq_qa)
,constraint FK_qaanswer foreign key(fk_seq_qa) references productqa(seq_qa)
);
-- Table QAANSWER이(가) 생성되었습니다.


-- @@ 리뷰 테이블 @@ --
create table review
(seq_review number not null
,fk_memberno number not null
,fk_productid varchar2(20) not null
,content varchar2(100) not null
,score number(1) not null
,constraint PK_review_seq_review primary key(seq_review)
,constraint FK_review_memberno foreign key(fk_memberno) references member(memberno)
,constraint FK_review_productid foreign key(fk_productid) references product(productid)
);
-- Table REVIEW이(가) 생성되었습니다.

create sequence review_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence REVIEW_SEQ이(가) 생성되었습니다.


-- @@ 찜하기 테이블 @@ --
create table likeproduct
(fk_memberno number not null
,fk_productid varchar2(20) not null
,status number(1) default 1 not null
,constraint PK_likeproduct primary key(fk_memberno, fk_productid)
,constraint FK_like_memberno foreign key(fk_memberno) references member(memberno)
,constraint FK_like_productid foreign key(fk_productid) references product(productid)
);
-- Table LIKEPRODUCT이(가) 생성되었습니다.


-- @@ 장바구니 테이블 @@ --
create table cart
(seq_cart number not null
,fk_memberno number not null
,fk_productid varchar2(20) not null
,checkstatus number(1) default 1 not null
,cartday date default sysdate not null
,constraint PK_cart_seq_cart primary key(seq_cart)
,constraint FK_cart_memberno foreign key(fk_memberno) references member(memberno)
,constraint FK_cart_productid foreign key(fk_productid) references product(productid)
);
-- Table CART이(가) 생성되었습니다.

create sequence cart_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence CART_SEQ이(가) 생성되었습니다.


-- @@ 자주 묻는 질문 테이블 @@ --
create table faq
(seq_faq number not null
,faqcategory varchar2(20) not null
,title varchar2(20) not null
,content varchar2(100) not null
,constraint PK_faq_seq_faq primary key(seq_faq)
,constraint UQ_faq_title unique(title)
);
-- Table FAQ이(가) 생성되었습니다.

create sequence faq_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence FAQ_SEQ이(가) 생성되었습니다.


-- @@ 관리자 테이블 @@ --
create table manager
(managerno number not null
,managerid varchar2(20) not null
,managerpwd varchar2(200) not null
,managertype varchar2(30) not null
,manageremail varchar2(200) not null
,managermobile varchar2(200) not null
,constraint PK_manager_managerno primary key(managerno)
,constraint UQ_manager_managerid unique(managerid)
);

-- Table MANAGER이(가) 생성되었습니다.

create sequence manager_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence MANAGER_SEQ이(가) 생성되었습니다.


-- @@ 검색 테이블 @@ --
create table keywordsearch
(keyword varchar2(30) not null
,searchcnt number default 0 not null
,constraint PK_keywordsearch primary key(keyword)
);
-- Table KEYWORDSEARCH이(가) 생성되었습니다.


-- @@ 이벤트 테이블 @@ --
create table event
(seq_event number not null
,eventname varchar2(50) not null
,fk_productid varchar2(20) not null
,startday date default sysdate not null
);

create sequence event_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

alter table event add endday date default add_months(sysdate, 1) not null;
alter table event drop column period;

select *
from event;


-- @@ 회원 테이블 @@ --
create table member
(memberno number not null
,userid varchar2(20) not null
,pwd varchar2(200) not null
,name varchar2(30) not null
,email varchar2(200) not null
,mobile varchar2(200)
);


-- @@ 쿠폰 테이블 @@ --
create table coupon
(couponcode varchar2(20) not null
,couponname varchar2(30) not null
,discount number(10) not null
,minprice number(20) not null
,fk_membershipname varchar2(10) not null
,constraint PK_coupon_couponcode primary key(couponcode)
,constraint FK_coupon_membershipname foreign key(fk_membershipname) references membership(membershipname)
);
-- Table COUPON이(가) 생성되었습니다.

select *
from coupon;


-- @@ 개별쿠폰 테이블 @@ --
create table eachcoupon
(eachcouponcode varchar2(20) not null
,fk_couponcode varchar2(20) not null
,fk_memberno number not null
,status number(1) default 0 not null
,endday date default add_months(sysdate, 1) not null
,constraint PK_eachcoupon primary key(eachcouponcode)
,constraint FK_eachcoupon_coupon foreign key(fk_couponcode) references coupon(couponcode)
,constraint FK_eachcoupon_member foreign key(fk_memberno) references member(memberno)
);
-- Table EACHCOUPON이(가) 생성되었습니다.

select *
from eachcoupon;


-- @@ 이벤트 테이블 @@ --
create table event
(seq_event number not null
,eventname varchar2(50) not null
,fk_productid varchar2(20) not null
,period number(5) not null
,startday date default sysdate not null
,constraint PK_event_seq_event primary key(seq_event)
,constraint FK_event_productid foreign key(fk_productid) references product(productid)
);
-- Table EVENT이(가) 생성되었습니다.

create sequence event_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence EVENT_SEQ이(가) 생성되었습니다.


-- @@ 이벤트 참여 테이블 @@ --
create table joinevent
(seq_joinevent number not null
,fk_event number not null
,fk_memberno number not null
,winstatus number(1) default 0 not null
,constraint PK_joinevent_seq_joinevent primary key(seq_joinevent)
,constraint FK_joinevent_event foreign key(fk_event) references event(seq_event)
,constraint FK_joinevent_memberno foreign key(fk_memberno) references member(memberno)
);
-- Table JOINEVENT이(가) 생성되었습니다.

create sequence joinevent_seq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence JOINEVENT_SEQ이(가) 생성되었습니다.

desc onequery;

select *
from tabs;

select *
from member;

desc member;

insert into manager(managerno, managerid, managerpwd, managertype, manageremail, managermobile)
values(manager_seq.nextval, 'admin', 'qwer1234$', '총관리자', 'admin@gmail.com', '010-1000-0001');

-- @@@ 멤버 프로시저
create or replace procedure pcd_member_insert
(p_userid   in varchar2
,p_name     in varchar2)
is
begin
    for i in 1..100 loop
        insert into member(memberno, userid, pwd, name, email, mobile, birthday, agreeemail, agreesms, agreethird)
        values(member_seq.nextval, p_userid||i, '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', p_name||i, 'MittOu5FojyHzFRTvo4WdmktaenWR+6x4i0hc8DlXUY=', 'J75gslC+PpI2/ugWLWOFMA==', '2020-11-24', 0, 0, 0);
    end loop;
end pcd_member_insert;
-- Procedure PCD_MEMBER_INSERT이(가) 컴파일되었습니다.

exec pcd_member_insert('leess','이순신');
-- PL/SQL 프로시저가 성공적으로 완료되었습니다.

commit;

select memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point, registerday, lastpwdchangeday, idle, status, dropday
from member;

select memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point, registerday, lastpwdchangeday, idle, status, dropday
from
    (
    select rownum AS rno, memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point, registerday, lastpwdchangeday, idle, status, dropday
    from
        (
        select memberno, userid, pwd, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress, agreeemail, agreesms, agreethird, fk_membershipname, point
             , to_char(registerday, 'yyyy-mm-dd') AS registerday
             , to_char(lastpwdchangeday, 'yyyy-mm-dd') AS lastpwdchangeday
             , idle, status, dropday
        from member
        where birthday like '%'||11||'%'
        order by registerday desc
        ) V
    ) T
where T.rno between 4 and 6;


select memberno, userid, birthday, agreeemail, agreesms, fk_membershipname, registerday, idle, status
from
    (
    select rownum AS rno, memberno, userid, birthday, agreeemail, agreesms, fk_membershipname, registerday, idle, status
    from
        (
        select memberno, userid, birthday, agreeemail, agreesms, fk_membershipname
             , to_char(registerday, 'yyyy-mm-dd') AS registerday
             , idle, status
        from member
        order by registerday desc
        ) V
    ) T
where T.rno between 4 and 6;


select productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price
from product P join productoption O
on P.productid = O.fk_productid
where fk_category like '%'||'mou'||'%'
order by fk_category;


select productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price
from
    (
    select rownum AS rno, productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price
    from
        (
        select productserialid, fk_productid, color, stock, saleday, productname, fk_category, character, price
        from product P join productoption O
        on P.productid = O.fk_productid
        where fk_category like '%'||'mou'||'%'
        order by productserialid
        ) V
    ) T
where T.rno between 4 and 6;

select ceil(count(*)/10)
from product P join productoption O
on P.productid = O.fk_productid;


select memberno, userid, name, email, mobile, birthday, postcode, address, detailaddress, extraaddress
     , case when agreeemail = '1' then '수신' else '거절' end AS agreeemail
     , case when agreesms = '1' then '수신' else '거절' end AS agreesms
     , case when agreethird = '1' then '동의' else '거절' end AS agreethird
     , fk_membershipname, point
     , to_char(registerday, 'yyyy-mm-dd') AS registerday
     , to_char(lastpwdchangeday, 'yyyy-mm-dd') AS lastpwdchangeday
     , idle, status, dropday
from member
where memberno = 2;

select *
from product P join productoption O
on P.productid = O.fk_productid;

select *
from purchase;


select purchasedetailid, fk_productserialid, volume, price, deliverystatus, imgfilename
from purchasedetail D;
join product R
on D.fk_productid = R.productid
where fk_purchaseno = 1;

select purchaseno, fk_memberno, name, receiver, P.postcode, P.address, P.detailaddress, P.extraaddress, payment, totalprice, purchaseday
from purchase P
join member M
on P.fk_memberno = M.memberno
where purchaseno = 1;

select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday
from
(
select rownum AS rno, fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday
from
 (
 select fk_couponcode, eachcouponcode, couponname, discount, minprice, fk_membershipname, fk_memberno, status, endday
 from eachcoupon E right join coupon C
 on E.fk_couponcode = C.couponcode
 order by endday desc
 ) V
) T
where T.rno between ? and ?;


select fk_memberno, category, title
from
 (
 select row_number() over(order by seq_oq asc) AS rno, fk_memberno, category, title
 from onequery O
 join member M
 on O.fk_memberno = M.memberno
 where to_char(writeday, 'yyyy-mm-dd') = '2020-11-15'
 ) v
where rno between '1' and '3';

select *
from manager;

desc productqa;

select *
from product;

insert into productqa(seq_qa, fk_memberno, fk_productid, content)
values(productqa_seq.nextval, 61, 'X50', '스피커 문의합니다');

commit;

select *
from cart;

desc cart;

update onequery set writeday = sysdate
where seq_oq = 61;



select *
from productqa;

select *
from loginhistory;

update onequery set answerstatus = 0
where seq_oq = 61;

delete from qaanswer;
commit;

ALTER TABLE oqanswer add constraint PK_oqanswer_seq_oq primary key(seq_oq);
commit;

select *
from TABS;

alter table oqanswer add FK_oqanwer_seq_oq foreign key(seq_oq) references onequery(seq_oq);

select seq_oq, userid, category, title, answerstatus
from
 (
 select row_number() over(order by seq_oq asc) AS rno, seq_oq, userid, category, title
 , case when answerstatus = '0' then '대기' else '완료' end AS answerstatus
 from onequery O
 join member M
 on O.fk_memberno = M.memberno
 where to_char(writeday, 'yyyy-mm-dd') = ?
 ) V;
where rno between ? and ?

select *
from member;

update member set dropday = sysdate
where memberno = 114;

commit;

select *
from
(
select B.userid, nvl(dropday, add_months(sysdate, 9999)) AS dropday, lastloginday
from
(
select userid, max(logindate) AS lastloginday
from member M
join loginhistory L
on M.userid = L.fk_userid
group by userid
) V
join member B
on V.userid = B.userid
) W
where dropday != add_months(sysdate, 9999)


update productoption set stock = stock + 5
where productserialid = 'X100_1'

rollback;
--------------------------------------------------------------------------------------------------------------------------------
desc purchasedetail;

select fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount
from
(
select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice
from purchasedetail D
join purchase P
on D.fk_purchaseno = P.purchaseno
join product R
on D.fk_productid = R.productid
order by purchaseday desc
) V
group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice
) W
) X
where X.rno between 1 and 3;


update purchase set totalprice = 1106000
where purchaseno = 4

commit;

alter table purchase add totalstatus varchar2(20) default '진행중' not null;

desc purchase; 
commit;

select *
from purchase;

select *
from productoption;

select *
from member;

desc coupon;

select *
from eachcoupon
where fk_memberno = 2;

14

insert into coupon(couponcode, couponname, discount, minprice, fk_membershipname)
values('cp_cclass', 'C클래스 전용 쿠폰', 5000, 50000, 'WELCOME');

insert into eachcoupon(eachcouponcode, fk_couponcode, fk_memberno)
values('cp_cclass_111901', 'cp_cclass', 2);

commit;
--------------------------------------------------------------------------------------------------------------------------------
select *
from manager;

update manager set managerpwd = '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382'
where managerno = 1;

commit;

select *
from manager;

select seq_event, eventname, fk_productid, startday, endday
from
(
select rownum AS rno, seq_event, eventname, fk_productid, startday, endday
from
 (
 select distinct seq_event, eventname, fk_productid, startday, endday
 from event E
 join joinevent J
 on E.seq_event = J.fk_event
 order by seq_event desc
 ) V
) T
where T.rno between 1 and 3;


"order by seq_event desc\n"+
") W\n"+
") X\n"+
"where X.rno between 1 and 3;";

insert into joinevent(seq_joinevent, fk_event, fk_memberno, eventcomment)
values(joinevent_seq.nextval, 4, 2, '테스트');

commit;

update event set endday = sysdate - 1
where seq_event = 4;


select seq_event, eventname, fk_productid, startday, endday, joincnt
from
(
select rownum AS rno, seq_event, eventname, fk_productid, startday, endday, nvl(joincnt, 0) AS joincnt
from
(
select seq_event, eventname, fk_productid, startday, endday, joincnt
from
(
select fk_event, count(*) AS joincnt
from joinevent
group by fk_event
) V
right join event E
on V.fk_event = E.seq_event
 order by seq_event desc
) W
) X
where X.rno between 1 and 10

select *
from product;

update manager set manageremail = 'oJweGomtVTjJZIJStNTI27fh25uk37VIu6qRLCNds2I=', managermobile = 'VM7+Mev2+F8cIvt9D6OQ5g=='
where managerno = 1;

commit;

select count(*)
from eachcoupon


create or replace procedure pcd_member_insert2
(p_userid   in varchar2
,p_name     in varchar2)
is
begin
    for i in 1..19 loop
        insert into member(memberno, userid, pwd, name, email, mobile, birthday, agreeemail, agreesms, agreethird, registerday)
        values(member_seq.nextval, p_userid||i, '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', p_name||i, 'MittOu5FojyHzFRTvo4WdmktaenWR+6x4i0hc8DlXUY=', 'J75gslC+PpI2/ugWLWOFMA==', '2020-11-24', 0, 0, 0, sysdate);
    end loop;
end pcd_member_insert2;
-- Procedure PCD_MEMBER_INSERT이(가) 컴파일되었습니다.

exec pcd_member_insert2('kfive','김다섯');
-- PL/SQL 프로시저가 성공적으로 완료되었습니다.

commit;

select W.dt AS registerday, nvl(sum(cnt), 0) AS cnt
from
(
select to_char(registerday, 'yyyymmdd') AS registerday, count(*) AS cnt
from member
where to_char(registerday, 'yyyymmdd') between (to_char(sysdate-6, 'yyyymmdd')) and (to_char(sysdate, 'yyyymmdd'))
group by to_char(registerday, 'yyyymmdd')
order by registerday desc
) V,
(
select to_char(to_date(to_char(sysdate-6, 'yyyymmdd'), 'yyyymmdd') + level - 1, 'yyyymmdd') AS dt
from dual
connect by level <= (to_date(to_char(sysdate, 'yyyymmdd'), 'yyyymmdd') - to_date(to_char(sysdate-6, 'yyyymmdd'), 'yyyymmdd') + 1)
) W
where W.dt = V.registerday(+)
group by W.dt
order by W.dt desc;

select fk_productserialid, sum
from
(
select row_number() over(order by sum desc) AS rno, fk_productserialid, sum
from
(
select fk_productserialid, sum(volume) AS sum
from
(
select fk_productserialid, volume
from purchasedetail
) V
group by fk_productserialid
) W
) X
where X.rno between 1 and 10

select count(*) AS cnt
from joinevent J
join
(
select seq_event
from
(
select row_number() over(order by seq_event desc) AS rno, seq_event
from event
where to_char(endday, 'yyyymmdd') >= to_char(sysdate, 'yyyymmdd')
) V
where V.rno = 1
) W
on J.fk_event = W.seq_event
group by fk_event


update manager set managertype = '전체'
where managerno = 1;

commit;

select fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, discount, totalstatus
from
(
select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus
from purchasedetail D
join purchase P
on D.fk_purchaseno = P.purchaseno
join product R
on D.fk_productid = R.productid
        order by fk_purchaseno desc
) V
group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus
) W
) X
where X.rno between 1 and 5

select *
from purchasedetail U
join productoption R
on U.fk_productserialid = R.productserialid
where fk_purchaseno = 4

select *
from product

select *
  from purchasedetail D
  join purchase P
  on D.fk_purchaseno = P.purchaseno
  join product R
  on D.fk_productid = R.productid


select fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, discount, totalstatus
from
(
select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus
from purchasedetail D
join purchase P
on D.fk_purchaseno = P.purchaseno
join product R
on D.fk_productid = R.productid
order by fk_purchaseno desc
) V
group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus
) W
) X
where X.rno between 1 and 10

String sql = "select ceil(count(*)/10)\n"+
"from\n"+
"(\n"+
"select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus\n"+
"from\n"+
"(\n"+
"select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus\n"+
"from\n"+
"(\n"+
"select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus\n"+
"from purchasedetail D\n"+
"join purchase P\n"+
"on D.fk_purchaseno = P.purchaseno\n"+
"join product R\n"+
"on D.fk_productid = R.productid\n"+
"order by fk_purchaseno desc\n"+
") V\n"+
"group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus\n"+
") W\n"+
") X";

select *
from event;

select seq_event, seq_joinevent, userid, eventcomment, winstatus
from event E
left join joinevent J
on E.seq_event = J.fk_event
left join member M
on J.fk_memberno = M.memberno
where seq_event = 6;

select *
from joinevent


select fk_event, seq_joinevent, userid, eventcomment, winstatus
from event E
left join joinevent J
on J.fk_event = E.seq_event
left join member M
on J.fk_memberno = M.memberno
where fk_event = 6;

select *
from product

update product set productid = 'MX ERGO 2'
where productid = 'MX ERGO TEST';

select *
from productoption

delete from productoption
where fk_productid = 'MX ERGO TEST'

commit;


select fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, discount, totalstatus
from
(
select rownum AS rno, fk_purchaseno, fk_memberno, payment, purchaseday, sumprice, totalprice, sumprice - totalprice AS discount, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, sum(sumprice) AS sumprice, totalprice, totalstatus
from
(
select fk_purchaseno, fk_memberno, payment, purchaseday, price * volume AS sumprice, totalprice, totalstatus
from purchasedetail D
join purchase P
on D.fk_purchaseno = P.purchaseno
join product R
on D.fk_productid = R.productid
) V
group by fk_purchaseno, fk_memberno, payment, purchaseday, totalprice, totalstatus
order by fk_purchaseno desc
) W
) X
where X.rno between 1 and 10;

select *
from purchase

update purchase set address = '서울 강남구 강남대로 238', detailaddress = '101동 101호', extraaddress = '(도곡동)';
commit;

select *
from member;

update joinevent set eventcomment = '꼭 당첨되고 싶어요'
where seq_joinevent = 8;

insert into joinevent(seq_joinevent, fk_event, fk_memberno, winstatus, eventcomment)
values(joinevent_seq.nextval, 4, 143, default, '항상 좋은 이벤트 감사합니다.')

commit;

select *
from tbl_point P
join purchasedetail D
on P.fk_purchaseno = D.fk_purchaseno


select *
from purchase;

String sql = "select purchase_seq.CURRVAL as fk_purchaseno\n"+
"from dual;";

select purchase_seq.nextval() from dual

insert into purchase_seq

SELECT purchase_seq.CURRVAL FROM DUAL;

select *
from tbl_point;

insert into purchase(purchaseno, fk_memberno, receiver, postcode, address, detailaddress, extraaddress, payment, totalprice, ordernum)
values(purchase_seq.nextval, 9, '최은지', '1189', '서울 강남구 강남대로 238', '101동 101호', '(도곡동)', 'card', 35000, purchase_seq.nextval||'-'||to_char(sysdate, 'yymmdd'));

commit;

String sql = "select sum(pointnum)\n"+
"from tbl_point T\n"+
"left join purchase P\n"+
"on P.purchaseno = T.fk_purchaseno\n"+
"left join member M\n"+
"on P.fk_memberno = M.memberno\n"+
"where P.fk_memberno = '9'";

select *
from member

update productoption set stock = 20
where productserialid in ('X100_4', 'X100_3', 'X100_2', 'X100_1', 'X50_3', 'X50_4', 'X50_5', 'X300_1', 'X300_2', 'X300_3');

commit;

select *
from onequery

select *
from productqa

insert into productqa(seq_qa, fk_memberno, fk_productid, content)
values(productqa_seq.nextval, 61, 'X50', '스피커 문의합니다');

commit;

insert into onequery(seq_oq, fk_memberno, category, title, content, answerform)
values(onequery_seq.nextval, 2, '회원등급', '회원등급 갱신', '회원등급 갱신일이 정확히 몇일인가요?', 0);


select *
from member
order by registerday

update member set status = 1
where memberno = 114

commit;

update member set status = 2
where userid = 'hjun341' and dropday <= sysdate

rollback;

