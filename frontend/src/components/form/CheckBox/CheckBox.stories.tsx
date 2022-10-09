import { ComponentMeta, ComponentStory } from "@storybook/react";
import CheckBox from "./CheckBox";

export default {
  title: "form/CheckBox",
  component: CheckBox,
} as ComponentMeta<typeof CheckBox>;

const Template: ComponentStory<typeof CheckBox> = (args) => <CheckBox {...args} />;

export const Default = Template.bind({});
Default.args = {
  label: "체크박스",
};

export const Checked = Template.bind({});
Checked.args = {
  label: "체크박스",
  checked: true,
};
