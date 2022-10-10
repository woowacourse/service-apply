import { Mission } from "../../../../types/domains/recruitments";

type PullRequestUrlProps = {
  judgment: Mission["judgment"];
};

const PullRequestUrl = ({ judgment }: PullRequestUrlProps) => {
  if (judgment === null) {
    return <span>Pull request 주소 : - </span>;
  }

  const pullRequestUrl = judgment.pullRequestUrl;

  return (
    <div>
      <span>Pull request 주소 : {pullRequestUrl}</span>
    </div>
  );
};

export default PullRequestUrl;
