-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Member addresses should be only one at any time.
-- - - - - - - - - - -/
select adr.MEMBER_ADDRESS_ID, adr.MEMBER_ID
     , adr.VALID_BEGIN_DATE, adr.VALID_END_DATE
     , adr.ADDRESS
from MEMBER_ADDRESS adr
where exists (select subadr.MEMBER_ADDRESS_ID
              from MEMBER_ADDRESS subadr
              where subadr.MEMBER_ID = adr.MEMBER_ID
                and subadr.VALID_BEGIN_DATE > adr.VALID_BEGIN_DATE
                and subadr.VALID_BEGIN_DATE < adr.VALID_END_DATE
)
;
