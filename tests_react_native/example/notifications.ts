import { AndroidStyle, Notification, AndroidLaunchActivityFlag } from '@notifee/react-native';

export const notifications: { key: string; notification: Notification }[] = [
  {
    key: 'Empty',
    notification: {
      android: {
        channelId: 'default',
        sound: 'horse.mp3',
      },
    },
  },

  {
    key: 'Basic',
    notification: {
      title: 'Title',
      body: 'Body of the notification',
      android: {
        // ongoing: true,
        channelId: 'foo',

        // style: {
        //   type: 1,
        //   text: 'Helllllllo World!!!!!!!!',
        // },

        // pressAction: {
        //   id: 'testing',
        //   reactComponent: 'test_component',
        // },
      },
    },
  },

  {
    key: 'Subtitle',
    notification: {
      title: 'Title',
      body: 'Body of the notification',
      subtitle: 'Subtitle text',
      android: {
        channelId: 'foo',
      },
    },
  },

  {
    key: 'Color',
    notification: {
      title: 'Color',
      body: 'Only the small icon should change color',
      android: {
        color: '#9c27b0',
        channelId: 'foo',
      },
    },
  },
  {
    key: 'Actions (event only)',
    notification: {
      title: 'Actions',
      body: 'Notification with actions',
      ios: {
        categoryId: 'actions',
      },
      android: {
        autoCancel: true,
        color: '#9c27b0',
        onlyAlertOnce: true,
        // inputHistory: ['Hey', 'Food tonight?'],
        actions: [
          {
            title: 'Reply, Open & Cancel',
            icon: 'https://invertase.io/icons/icon-48x48.png',
            pressAction: {
              id: 'first_action',
              // reactComponent: 'test_component',
            },
            input: {},
            // input: {
            //   choices: ['Hey'],
            //   allowFreeFormInput: true,
            //   editableChoices: true,
            //   placeholder: 'Reply...',
            // },
          },
          {
            title: 'Open & Cancel',
            icon: 'https://invertase.io/icons/icon-48x48.png',
            pressAction: {
              id: 'second_action',
              mainComponent: 'test_component',
            },
            // input: {
            //   choices: ['You'],
            //   allowFreeFormInput: true,
            //   placeholder: 'Second Input',
            // },
          },
          {
            title: 'Nothing',
            icon: 'https://invertase.io/icons/icon-48x48.png',
            pressAction: {
              id: 'third_action',
            },
          },
        ],
        channelId: 'foo',
      },
    },
  },
  {
    key: 'Service',
    notification: {
      title: 'Service',
      body: 'Notification Service',
      android: {
        autoCancel: true,
        color: '#9c27b0',
        onlyAlertOnce: true,
        asForegroundService: true,
        actions: [
          {
            title: 'Stop',
            icon: 'https://invertase.io/icons/icon-48x48.png',
            pressAction: {
              id: 'stop',
            },
          },
        ],
        channelId: 'foo',
      },
    },
  },

  {
    key: 'Big Picture Style',
    notification: {
      title: 'Big Picture Style',
      body: 'Expand for a cat',
      android: {
        channelId: 'foo',
        style: {
          type: AndroidStyle.BIGPICTURE,
          picture: 'https://icatcare.org/app/uploads/2018/07/Thinking-of-getting-a-cat.png',
        },
      },
      ios: {
        attachments: [
          {
            id: 'image',
            url: 'media/cat.png',
            thumbnailHidden: true,
            thumbnailClippingRect: {
              x: 0.1,
              y: 0.1,
              width: 0.1,
              height: 0.1,
            },
          },
        ],
      },
    },
  },
  {
    key: 'iOS Video',
    notification: {
      title: 'iOS Attachments: Video',
      body: 'Expand to see a video',
      ios: {
        attachments: [
          {
            id: 'video',
            url: 'media/movie.mp4',
            thumbnailTime: 1,
          },
        ],
      },
    },
  },
  {
    key: 'iOS GIF',
    notification: {
      title: 'iOS Attachments: GIF',
      body: 'Expand to see a gif',
      ios: {
        attachments: [
          {
            id: 'gif',
            url: 'media/back.gif',
            thumbnailTime: 6,
          },
        ],
      },
    },
  },
  {
    key: 'Android Launch Activity Flags',
    notification: {
      title: 'Testing SINGLE_TOP launch.',
      body: 'Expand for a cat!',
      android: {
        channelId: 'foo',
        pressAction: {
          id: 'default',
          launchActivity: 'default',
          launchActivityFlags: [AndroidLaunchActivityFlag.SINGLE_TOP],
        },
        lights: ['#ffffff', 25, 50],
        style: {
          type: AndroidStyle.BIGPICTURE,
          picture: 'https://icatcare.org/app/uploads/2018/07/Thinking-of-getting-a-cat.png',
        },
      },
    },
  },
];
