import SearchModal from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/SearchModal',
  component: SearchModal,
} as ComponentMeta<typeof SearchModal>;

const Template: ComponentStory<typeof SearchModal> = args => <SearchModal {...args} />;

export const SearchModalTemplate = Template.bind({});
SearchModalTemplate.args = {
  handleSearchModal: () => {},
};
