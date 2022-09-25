import { ComponentMeta, ComponentStory } from "@storybook/react";
import Panel, { PanelProps } from "./Panel";

export default {
  title: "components/Panel",
  component: Panel,
} as ComponentMeta<typeof Panel>;

const Template: ComponentStory<typeof Panel> = (args: PanelProps) => <Panel {...args}>본문</Panel>;

export const Default = Template.bind({});

Default.args = {
  title: "웹 백엔드 4기",
};
