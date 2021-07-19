import React from 'react';
import Field from './Field';

export default {
  title: 'form/Field',
  component: Field,
};

const Template = (args) => <Field {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: 'Filed의 내용',
};
