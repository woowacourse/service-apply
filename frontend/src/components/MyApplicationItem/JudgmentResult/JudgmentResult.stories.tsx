import { ComponentMeta, ComponentStory } from "@storybook/react";
import JudgmentResult from "./JudgmentResult";

export default {
  title: "components/JudgmentResult",
  component: JudgmentResult,
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
} as ComponentMeta<typeof JudgmentResult>;

const Template: ComponentStory<typeof JudgmentResult> = (args) => <JudgmentResult {...args} />;

export const NoResultText = Template.bind({});
NoResultText.args = {
  judgment: null,
};

export const PendingResultText = Template.bind({});
PendingResultText.args = {
  judgment: {
    pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "STARTED",
    passCount: 0,
    totalCount: 0,
    message: "빌드를 실패했습니다.",
    startedDateTime: "2020-10-25T15:00:00",
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const PassResultText = Template.bind({});
PassResultText.args = {
  judgment: {
    pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "SUCCESS",
    passCount: 5,
    totalCount: 5,
    message: "",
    startedDateTime: "2020-10-25T15:00:00",
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const NoPassResultText = Template.bind({});
NoPassResultText.args = {
  judgment: {
    pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "SUCCESS",
    passCount: 4,
    totalCount: 5,
    message: "",
    startedDateTime: "2020-10-25T15:00:00",
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};

export const FailJudgmentMission = Template.bind({});
FailJudgmentMission.args = {
  judgment: {
    pullRequestUrl: "https://github.com/woowacourse/service-apply/pull/367",
    commitHash: "642951e1324eaf66914bd53df339d94cad5667e3",
    status: "FAIL",
    passCount: 0,
    totalCount: 0,
    message: "빌드에 실패했습니다",
    startedDateTime: "2020-10-25T15:00:00",
    commitUrl:
      "https://github.com/woowacourse/service-apply/pull/367/commits/642951e1324eaf66914bd53df339d94cad5667e3",
  },
};
