import { ComponentMeta, ComponentStory } from "@storybook/react";
import GenderField from "./GenderField";

export default {
  title: "form/GenderField",
  component: GenderField,
} as ComponentMeta<typeof GenderField>;

const Template: ComponentStory<typeof GenderField> = (args) => <GenderField {...args} />;

export const Default = Template.bind({});
Default.args = {};
