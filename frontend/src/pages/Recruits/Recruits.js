import { useMemo, useState } from "react";
import { generatePath, useNavigate, useLocation } from "react-router-dom";
import TabItem from "../../components/@common/TabItem/TabItem";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { PATH, PARAM } from "../../constants/path";
import { RECRUITMENT_STATUS } from "../../constants/recruitment";
import { COURSE_TAB, COURSE_TAB_LIST, RECRUITS_TAB } from "../../constants/tab";
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
  const [courseTabStatus, setCourseTabStatus] = useState(COURSE_TAB.ALL.label);
  const navigate = useNavigate();

  const query = new URLSearchParams(useLocation().search);
  const selectedTab = query.get("status") ?? RECRUITS_TAB.ALL.name;

  const { recruitment } = useRecruitmentContext();

  const filteredRecruitment = useMemo(() => {
    const sortedRecruitmentItem = recruitment[selectedTab].sort((a, b) => {
      return new Date(b.startDateTime) - new Date(a.startDateTime);
    });

    if (courseTabStatus === COURSE_TAB.ALL.label) {
      return sortedRecruitmentItem;
    }

    if (courseTabStatus === COURSE_TAB.WOOWA_TECH_COURSE.label) {
      return sortedRecruitmentItem.filter((recruitmentItem) =>
        recruitmentItem.title.includes(courseTabStatus)
      );
    }

    return sortedRecruitmentItem.filter((recruitmentItem) => {
      const fullCourseNameArray = recruitmentItem.title.split(" ");
      const courseName = fullCourseNameArray.slice(0, fullCourseNameArray.length - 1);

      return courseName.join(" ").trim() === courseTabStatus;
    });
  }, [recruitment, selectedTab, courseTabStatus]);

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
      <div className={styles["recruitment-list-box"]}>
        <div className={styles["course-tab-list"]}>
          {COURSE_TAB_LIST.map(({ name, label }) => (
            <TabItem
              key={name}
              checked={label === courseTabStatus}
              onClickTabItem={() => {
                setCourseTabStatus(label);
              }}
            >
              {label}
            </TabItem>
          ))}
        </div>
        {recruitment && (
          <div className={styles["recruitment-list"]} role="list">
            {filteredRecruitment.length === 0 ? (
              <div className={styles["empty-state-box"]}>해당하는 모집이 없습니다.</div>
            ) : (
              filteredRecruitment.map((recruitment) => (
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
