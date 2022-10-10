import { generatePath, useNavigate } from "react-router-dom";
import useTokenContext from "../../hooks/useTokenContext";
import useRecruitList from "./../../hooks/useRecruitList";
import TabItem from "../../components/@common/TabItem/TabItem";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { generateQuery } from "../../utils/route/query";
import { PATH, PARAM } from "../../constants/path";
import { PROGRAM_TAB, PROGRAM_TAB_LIST } from "../../constants/tab";
import styles from "./Recruits.module.css";

const Recruits = () => {
  const { token } = useTokenContext();
  const navigate = useNavigate();

  const { programTabStatus, setProgramTabStatus, filteredRecruitments } = useRecruitList();

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
          {programTabStatus.name === PROGRAM_TAB.ALL.name
            ? `우아한교육프로그램과\n함께할 개발자를 찾고 있어요!`
            : programTabStatus.label}
        </h1>
        <p className={styles["program-description"]}>{programTabStatus.description}</p>
      </div>

      <div className={styles["recruitment-list-box"]}>
        <h2 className={styles["recruitment-list-title"]}>지원하기</h2>
        <div className={styles["program-tab-list"]}>
          {PROGRAM_TAB_LIST.map((programTabListItem) => (
            <TabItem
              key={programTabListItem.name}
              checked={programTabListItem.label === programTabStatus.label}
              onClickTabItem={() => {
                setProgramTabStatus(programTabListItem);
              }}
            >
              {programTabListItem.label}
            </TabItem>
          ))}
        </div>
        {
          <div className={styles["recruitment-list"]} role="list">
            {filteredRecruitments.length === 0 ? (
              <div className={styles["empty-state-box"]}>해당하는 모집이 없습니다.</div>
            ) : (
              filteredRecruitments.map((recruitment) => (
                <RecruitmentItem
                  key={recruitment.id}
                  recruitment={recruitment}
                  onClickButton={() => goToNewApplicationFormPage(recruitment)}
                  role="listitem"
                />
              ))
            )}
          </div>
        }
      </div>
    </>
  );
};
export default Recruits;
