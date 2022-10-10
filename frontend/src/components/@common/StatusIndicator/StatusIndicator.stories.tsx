import { ComponentMeta, ComponentStory } from "@storybook/react";
import StatusIndicator from "./StatusIndicator";

export default {
  title: "components/StatusIndicator",
  component: StatusIndicator,
} as ComponentMeta<typeof StatusIndicator>;

const Template: ComponentStory<typeof StatusIndicator> = (args) => <StatusIndicator {...args} />;

export const Default = Template.bind({});
Default.args = {
  text: "지원하기",
  active: true,
};

export const ClosedRecruitment = Template.bind({});
ClosedRecruitment.args = {
  text: "모집종료",
  active: false,
};
