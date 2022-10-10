import { ComponentMeta, ComponentStory } from "@storybook/react";
import Radio from "./Radio";

export default {
  title: "form/Radio",
  component: Radio,
} as ComponentMeta<typeof Radio>;

const Template: ComponentStory<typeof Radio> = (args) => <Radio {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: "라디오 버튼",
};
