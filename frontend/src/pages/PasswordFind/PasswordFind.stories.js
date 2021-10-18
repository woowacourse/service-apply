import React from 'react';
import PasswordFind from './PasswordFind';

export default {
  title: 'pages/PasswordFind',
  component: PasswordFind,
};

const Template = (args) => <PasswordFind {...args} />;

export const Default = Template.bind({});

Default.args = {};
