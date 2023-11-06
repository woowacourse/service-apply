import { ComponentMeta, ComponentStory } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import { missionsDummy } from "./../../../mock/dummy";
import MyMissionItem from "./MyMissionItem";
import { MISSION_STATUS, MISSION_SUBMISSION_METHOD } from "../../../constants/recruitment";
import { ISO8601DateString } from "../../../../types/domains/common";

export default {
  title: "components/MyMissionItem",
  component: MyMissionItem,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/",
          },
        ]}
      >
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
      </MemoryRouter>
    ),
  ],
} as ComponentMeta<typeof MyMissionItem>;

const Template: ComponentStory<typeof MyMissionItem> = (args) => <MyMissionItem {...args} />;

export const NotSubmittedMission = Template.bind({});
NotSubmittedMission.args = {
  mission: missionsDummy["2"][0],
  recruitmentId: "1",
};

export const StartedJudgmentMission = Template.bind({});
StartedJudgmentMission.args = {
  mission: missionsDummy["2"][2],
  recruitmentId: "1",
};

export const NoPassJudgmentMission = Template.bind({});
NoPassJudgmentMission.args = {
  mission: missionsDummy["2"][5],
  recruitmentId: "7",
};

export const FailJudgmentMission = Template.bind({});
FailJudgmentMission.args = {
  mission: missionsDummy["2"][6],
  recruitmentId: "8",
};

export const PassJudgmentMission = Template.bind({});
PassJudgmentMission.args = {
  mission: missionsDummy["2"][4],
  recruitmentId: "6",
};

export const PrivateRepositorySubmission = Template.bind({});
PrivateRepositorySubmission.args = {
  mission: {
    id: 2,
    title: "비공개 저장소 제출 방식 과제",
    description: "설명",
    startDateTime: "2023-10-25T15:00:00" as ISO8601DateString,
    endDateTime: "2030-11-25T15:00:00" as ISO8601DateString,
    submitted: false,
    submittable: true,
    submissionMethod: MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY,
    status: MISSION_STATUS.SUBMITTING,
    runnable: true,
    judgment: null,
  },
  recruitmentId: "9",
};
