import { ComponentMeta, ComponentStory } from "@storybook/react";
import Description, { DescriptionProps } from "./Description";

export default {
  title: "form/Description",
  component: Description,
} as ComponentMeta<typeof Description>;

const Template: ComponentStory<typeof Description> = (args: DescriptionProps) => (
  <Description {...args} />
);

export const Default = Template.bind({});
Default.args = {
  children: "설명입니다!",
};
