import { Mission } from "../../../../types/domains/recruitments";
import styles from "./CommitHash.module.css";

type CommitHashProps = {
  judgment: Mission["judgment"];
};

const CommitHash = ({ judgment }: CommitHashProps) => {
  if (judgment === null) {
    return <span>예제 테스트 실행 Commit : - </span>;
  }

  const commitUrl = judgment.commitUrl;
  const shortCommitHash = `@${judgment.commitHash.slice(0, 6)}`;

  return (
    <div>
      <p>
        예제 테스트 실행 Commit :&nbsp;
        <a
          href={commitUrl}
          target="_blank"
          rel="noreferrer noopener"
          aria-label="GitHub Commit"
          className={styles["commit-hash"]}
          title={commitUrl}
        >
          {shortCommitHash}
        </a>
      </p>
    </div>
  );
};

export default CommitHash;
