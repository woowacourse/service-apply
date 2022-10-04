import { ComponentMeta, ComponentStory } from "@storybook/react";
import Button from "./IconButton";

export default {
  title: "components/IconButton",
  component: Button,
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args) => <Button {...args} />;

export const Default = Template.bind({});
Default.args = {
  src: "https://dummyimage.com/100x100",
};

export const Disabled = Template.bind({});
Disabled.args = {
  src: "https://dummyimage.com/100x100",
  disabled: true,
};
