import { ComponentMeta, ComponentStory } from "@storybook/react";
import Radio, { RadioProps } from "./Radio";

export default {
  title: "form/Radio",
  component: Radio,
} as ComponentMeta<typeof Radio>;

const Template: ComponentStory<typeof Radio> = (args: RadioProps) => <Radio {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: "라디오 버튼",
};
