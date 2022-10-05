import { useMemo, useState } from "react";
import { generatePath, useNavigate } from "react-router-dom";
import TabItem from "../../components/@common/TabItem/TabItem";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { PATH, PARAM } from "../../constants/path";
import { COURSE_TAB, COURSE_TAB_LIST, RECRUITS_TAB } from "../../constants/tab";
import useRecruitmentContext from "../../hooks/useRecruitmentContext";
import useTokenContext from "../../hooks/useTokenContext";
import { generateQuery } from "../../utils/route/query";
import styles from "./Recruits.module.css";

const Recruits = () => {
  const { token } = useTokenContext();
  const [courseTabStatus, setCourseTabStatus] = useState(COURSE_TAB.ALL);
  const navigate = useNavigate();

  const { recruitment } = useRecruitmentContext();

  const filteredRecruitment = useMemo(() => {
    const sortedRecruitmentItem = recruitment[RECRUITS_TAB.ALL.name].sort((a, b) => {
      return new Date(b.startDateTime) - new Date(a.startDateTime);
    });

    if (courseTabStatus.label === COURSE_TAB.ALL.label) {
      return sortedRecruitmentItem;
    }

    if (courseTabStatus.label === COURSE_TAB.WOOWA_TECH_COURSE.label) {
      return sortedRecruitmentItem.filter((recruitmentItem) =>
        recruitmentItem.title.includes(courseTabStatus.label)
      );
    }

    return sortedRecruitmentItem.filter((recruitmentItem) => {
      const fullCourseNameArray = recruitmentItem.title.split(" ");
      const courseName = fullCourseNameArray.slice(0, fullCourseNameArray.length - 1);

      return courseName.join(" ").trim() === courseTabStatus.label;
    });
  }, [recruitment, courseTabStatus]);

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
      <div className={styles["program-introduce-box"]}>
        <h1 className={styles["program-name"]}>
          {courseTabStatus.name === COURSE_TAB.ALL.name
            ? "메인 소개 타이틀"
            : courseTabStatus.label}
        </h1>
        <p className={styles["program-description"]}>{courseTabStatus.description}</p>
      </div>

      <div className={styles["recruitment-list-box"]}>
        <h2 className={styles["recruitment-list-title"]}>지원하기</h2>
        <div className={styles["course-tab-list"]}>
          {COURSE_TAB_LIST.map((courseTabItem) => (
            <TabItem
              key={courseTabItem.name}
              checked={courseTabItem.label === courseTabStatus.label}
              onClickTabItem={() => {
                setCourseTabStatus(courseTabItem);
              }}
            >
              {courseTabItem.label}
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
