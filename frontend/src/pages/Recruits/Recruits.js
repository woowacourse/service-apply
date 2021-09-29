import React from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import classNames from "classnames";

import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";

import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import { generateQuery } from "../../utils/route/query";
import PATH from "../../constants/path";
import { RECRUITS_TAB, RECRUITS_TAB_LIST } from "../../constants/tab";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";

import styles from "./Recruits.module.css";

const BUTTON_LABEL = {
  [RECRUITMENT_STATUS.RECRUITING]: "지원하기",
  [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
  [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
  [RECRUITMENT_STATUS.ENDED]: "모집 종료",
};

const Recruits = () => {
  const history = useHistory();

  const query = new URLSearchParams(useLocation().search);
  const selectedTab = query.get("status") ?? RECRUITS_TAB.ALL.name;

  const { recruitment } = useRecruitmentContext();

  const goApplicantsNewPage = (recruitmentId) => {
    history.push({
      pathname: PATH.NEW_APPLICATION,
      state: {
        recruitmentId,
      },
    });
  };

  return (
    <div className={styles.box}>
      <nav className={styles.tab}>
        <ul className={styles["tab-list"]}>
          {RECRUITS_TAB_LIST.map(({ name, label }) => (
            <li
              key={name}
              className={classNames(styles["tab-item"], {
                [styles.active]: name === selectedTab,
              })}
            >
              <Link
                to={{
                  pathname: PATH.RECRUITS,
                  search: generateQuery({ status: name }),
                }}
              >
                {label}
              </Link>
            </li>
          ))}
        </ul>
      </nav>

      {recruitment && (
        <div className={styles["recruitment-list"]} role="list">
          {recruitment[selectedTab].map((recruitment) => (
            <RecruitmentItem
              key={recruitment.id}
              recruitment={recruitment}
              activeButton={
                recruitment.status === RECRUITMENT_STATUS.RECRUITING
              }
              buttonLabel={BUTTON_LABEL[recruitment.status]}
              onClick={() => goApplicantsNewPage(recruitment.id)}
              role="listitem"
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Recruits;
