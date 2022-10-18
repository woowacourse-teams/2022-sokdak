import { withRouter } from 'storybook-addon-react-router-v6';

import SearchWeb from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Header/SearchWeb',
  component: SearchWeb,
  decorators: [withRouter],
} as ComponentMeta<typeof SearchWeb>;

const Template: ComponentStory<typeof SearchWeb> = () => {
  return <SearchWeb onClickSearchIcon={() => {}} />;
};

export const SearchWebTemplate = Template.bind({});
