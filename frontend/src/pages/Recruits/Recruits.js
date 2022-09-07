import classNames from "classnames";
import React, { useMemo } from "react";
import { generatePath, Link, useNavigate, useLocation } from "react-router-dom";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import PATH, { PARAM } from "../../constants/path";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import { RECRUITS_TAB, RECRUITS_TAB_LIST } from "../../constants/tab";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { generateQuery } from "../../utils/route/query";
import styles from "./Recruits.module.css";

const BUTTON_LABEL = {
  [RECRUITMENT_STATUS.RECRUITING]: "지원하기",
  [RECRUITMENT_STATUS.RECRUITABLE]: "모집 예정",
  [RECRUITMENT_STATUS.UNRECRUITABLE]: "일시 중지",
  [RECRUITMENT_STATUS.ENDED]: "모집 종료",
};

const Recruits = () => {
  const { token } = useTokenContext();
  const navigate = useNavigate();

  const query = new URLSearchParams(useLocation().search);
  const selectedTab = query.get("status") ?? RECRUITS_TAB.ALL.name;

  const { recruitment } = useRecruitmentContext();

  const sortedRecruitment = useMemo(
    () =>
      recruitment[selectedTab].sort((a, b) => {
        return new Date(b.startDateTime) - new Date(a.startDateTime);
      }),
    [recruitment, selectedTab]
  );

  const goToNewApplicationFormPage = (recruitment) => {
    if (!token) {
      navigate(
        { pathname: PATH.LOGIN },
        {
          state: {
            currentRecruitment: recruitment,
          },
        }
      );

      return;
    }

    navigate(
      {
        pathname: generatePath(PATH.APPLICATION_FORM, {
          status: PARAM.APPLICATION_FORM_STATUS.NEW,
        }),
        search: generateQuery({ recruitmentId: recruitment.id }),
      },
      {
        state: {
          currentRecruitment: recruitment,
        },
      }
    );
  };

  return (
    <>
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

      <div className={styles["recruitment-list-box"]}>
        {recruitment && (
          <div className={styles["recruitment-list"]} role="list">
            {sortedRecruitment.length === 0 ? (
              <div className={styles["empty-state-box"]}>해당하는 모집이 없습니다.</div>
            ) : (
              sortedRecruitment.map((recruitment) => (
                <RecruitmentItem
                  key={recruitment.id}
                  recruitment={recruitment}
                  isButtonDisabled={recruitment.status !== RECRUITMENT_STATUS.RECRUITING}
                  buttonLabel={BUTTON_LABEL[recruitment.status]}
                  onClickButton={() => goToNewApplicationFormPage(recruitment)}
                  role="listitem"
                />
              ))
            )}
          </div>
        )}
      </div>
    </>
  );
};

export default Recruits;
