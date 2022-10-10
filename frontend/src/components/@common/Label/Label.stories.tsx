import { ComponentMeta, ComponentStory } from "@storybook/react";
import Label from "./Label";

export default {
  title: "form/Label",
  component: Label,
} as ComponentMeta<typeof Label>;

const Template: ComponentStory<typeof Label> = (args) => <Label {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: "이름",
};

export const Required = Template.bind({});
Required.args = {
  required: true,
  children: "이름",
};
