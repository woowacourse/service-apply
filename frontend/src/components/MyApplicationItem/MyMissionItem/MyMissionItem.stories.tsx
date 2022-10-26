import { ComponentMeta, ComponentStory } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import { missionsDummy } from "./../../../mock/dummy";
import MyMissionItem from "./MyMissionItem";

export default {
  title: "components/MyMissionItem",
  component: MyMissionItem,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/'",
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
