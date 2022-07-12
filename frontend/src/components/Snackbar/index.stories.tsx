import Snackbar from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Snackbar',
  component: Snackbar,
} as ComponentMeta<typeof Snackbar>;

const Template: ComponentStory<typeof Snackbar> = args => <Snackbar {...args} />;

export const SnackbarTemplate: ComponentStory<typeof Snackbar> = Template.bind({});
SnackbarTemplate.args = {
  message: '글 작성이 완료되었습니다.',
};
