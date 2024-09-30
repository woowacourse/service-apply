import { ComponentMeta, ComponentStory } from "@storybook/react";
import { ISO8601DateString } from "../../../../types/domains/common";
import { startedJudgeDateTime } from "./../../../mock/dummy";
import JudgmentResultText from "./JudgmentResult";

export default {
  title: "components/JudgmentResult",
  component: JudgmentResultText,
  decorators: [
    (Story) => (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          width: "100%",
        }}
      >
        <Story />
      </div>
    ),
  ],
} as ComponentMeta<typeof JudgmentResultText>;

const Template: ComponentStory<typeof JudgmentResultText> = (args) => (
  <JudgmentResultText {...args} />
);

export const NoResultText = Template.bind({});
NoResultText.args = {
  judgment: null,
};

export const StartedResultText = Template.bind({});
StartedResultText.args = {
  judgment: {
    url: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "STARTED",
    passCount: 0,
    totalCount: 0,
    message: "",
    startedDateTime: startedJudgeDateTime as ISO8601DateString,
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const PassResultText = Template.bind({});
PassResultText.args = {
  judgment: {
    url: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "SUCCEEDED",
    passCount: 5,
    totalCount: 5,
    message: "",
    startedDateTime: startedJudgeDateTime as ISO8601DateString,
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const NoPassResultText = Template.bind({});
NoPassResultText.args = {
  judgment: {
    url: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "SUCCEEDED",
    passCount: 4,
    totalCount: 5,
    message: "",
    startedDateTime: startedJudgeDateTime as ISO8601DateString,
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const FailJudgmentMission = Template.bind({});
FailJudgmentMission.args = {
  judgment: {
    url: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "FAILED",
    passCount: 0,
    totalCount: 0,
    message: "빌드에 실패했습니다",
    startedDateTime: startedJudgeDateTime as ISO8601DateString,
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};
