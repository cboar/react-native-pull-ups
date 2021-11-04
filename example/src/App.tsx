import React, { useCallback, useState } from 'react';
import { View, Text, ScrollView, Button } from 'react-native';
import PullUp, { BottomSheetState } from 'react-native-pull-ups';

export default function App() {
  // const [scrollViewIsScrollable, setScrollViewIsScrollable] = useState(true);

  // const handleChange = (evt: OnChangeContext) => {
  //   const { isFullScreen } = evt.nativeEvent;
  //   if (isFullScreen) {
  //     setScrollViewIsScrollable(true);
  //   } else if (!isFullScreen) {
  //     setScrollViewIsScrollable(false);
  //   }
  // };

  const [active, setActive] = useState(false);

  const renderBackground = () =>
    new Array(100)
      .fill('')
      .map((_, i) => <Text key={`bg-${i}`}>Background {i}</Text>);

  const renderPullUpContent = () =>
    new Array(5)
      .fill('')
      .map((_, i) => <Text key={`content-${i}`}>Content {i}</Text>);

  const onPress = useCallback(() => {
    setActive(!active);
  }, [active]);

  const onLayout = useCallback(evt => {
    console.log(evt.nativeEvent);
  }, []);

  return (
    <ScrollView>
      <Button onPress={onPress} title="Toggle" />
      {renderBackground()}

      <PullUp active={active}>
        <View onLayout={onLayout} style={{ flexGrow: 0, flexShrink: 1 }}>
          {renderPullUpContent()}
        </View>
      </PullUp>

    </ScrollView>
  );
}
