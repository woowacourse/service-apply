import { ComponentMeta, ComponentStory } from "@storybook/react";
import Button, { ButtonProps } from "./Button";

export default {
  title: "form/Button",
  component: Button,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args: ButtonProps) => <Button {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: "버튼",
};

export const Outlined = Template.bind({});
Outlined.args = {
  variant: "outlined",
  children: "버튼",
};

export const Cancel = Template.bind({});
Cancel.args = {
  cancel: true,
  children: "취소",
};

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
  children: "버튼",
};
