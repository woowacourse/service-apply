import TabItem from "../../components/@common/TabItem/TabItem";
import RecruitmentItem from "../../components/RecruitmentItem/RecruitmentItem";
import { PROGRAM_TAB, PROGRAM_TAB_LIST } from "../../constants/tab";
import useRecruits from "../../hooks/useRecruits";
import styles from "./Recruits.module.css";

const Recruits = () => {
  const {
    tabs: { programTabStatus, setProgramTabStatus, filteredRecruitments },
    fetchMyApplication,
  } = useRecruits();

  return (
    <>
      <div className={styles["program-introduce-box"]}>
        <h1 className={styles["program-name"]}>
          {programTabStatus.name === PROGRAM_TAB.ALL.name
            ? `우아한형제들의 교육 프로그램과\n함께할 개발자를 찾고 있어요!`
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
                  onClickButton={() => fetchMyApplication(recruitment)}
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
