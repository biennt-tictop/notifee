package app.notifee.core.model;

/*
 * Copyright (c) 2016-present Invertase Limited & Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this library except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.core.app.Person;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.notifee.core.Logger;
import app.notifee.core.utility.ResourceUtils;

@Keep
public class NotificationAndroidShortcutModel {
  private static final String TAG = "NotificationAndroidShortcut";
  private Bundle mNotificationAndroidShortcutBundle;

  private NotificationAndroidShortcutModel(Bundle shortcutBundle) {
    mNotificationAndroidShortcutBundle = shortcutBundle;
  }

  public static NotificationAndroidShortcutModel fromBundle(Bundle shortcutBundle) {
    return new NotificationAndroidShortcutModel(shortcutBundle);
  }

  /**
   * Converts a person bundle from JS into a Person
   *
   * @param personBundle
   * @return
   */
  private static ListenableFuture<Person> getPerson(
    ListeningExecutorService lExecutor, Bundle personBundle) {
    return lExecutor.submit(
      () -> {
        Person.Builder personBuilder = new Person.Builder();

        personBuilder.setName(personBundle.getString("name"));

        if (personBundle.containsKey("id")) {
          personBuilder.setKey(personBundle.getString("id"));
        }

        if (personBundle.containsKey("bot")) {
          personBuilder.setBot(personBundle.getBoolean("bot"));
        }

        if (personBundle.containsKey("important")) {
          personBuilder.setImportant(personBundle.getBoolean("important"));
        }

        if (personBundle.containsKey("icon")) {
          String personIcon = Objects.requireNonNull(personBundle.getString("icon"));
          Bitmap personIconBitmap = null;

          try {
            personIconBitmap =
              ResourceUtils.getImageBitmapFromUrl(personIcon).get(10, TimeUnit.SECONDS);
          } catch (TimeoutException e) {
            Logger.e(
              TAG,
              "Timeout occurred whilst trying to retrieve a person icon: " + personIcon,
              e);
          } catch (Exception e) {
            Logger.e(
              TAG,
              "An error occurred whilst trying to retrieve a person icon: " + personIcon,
              e);
          }

          if (personIconBitmap != null) {
            personIconBitmap = ResourceUtils.getCircularBitmap(personIconBitmap);
            personBuilder.setIcon(IconCompat.createWithAdaptiveBitmap(personIconBitmap));
          }
        }

        if (personBundle.containsKey("uri")) {
          personBuilder.setUri(personBundle.getString("uri"));
        }

        return personBuilder.build();
      });
  }

  public Bundle toBundle() {
    return (Bundle) mNotificationAndroidShortcutBundle.clone();
  }

  public @Nullable Bundle getAction() {
    return mNotificationAndroidShortcutBundle.getBundle("action");
  }

  @Nullable
  public ListenableFuture<ShortcutInfoCompat.Builder> getShortcutInfoTask(
    ListeningExecutorService lExecutor, Context context, NotificationModel notificationModel) {
    return buildShortcutInfoTask(lExecutor, context, notificationModel);
  }

  /**
   * Gets a MessagingStyle for a notification
   */
  private ListenableFuture<ShortcutInfoCompat.Builder> buildShortcutInfoTask(
    ListeningExecutorService lExecutor, Context context, NotificationModel notificationModel) {
    return lExecutor.submit(
      () -> {
        String id = mNotificationAndroidShortcutBundle.getString("id");
        if (id == null) {
          return null;
        }

        ShortcutInfoCompat.Builder shortcutInfoBuilder = new ShortcutInfoCompat.Builder(context, id);
        String icon = mNotificationAndroidShortcutBundle.getString("icon");
        if (icon != null && !icon.isEmpty()) {
          Bitmap iconBitmap = null;

          try {
            iconBitmap =
              ResourceUtils.getImageBitmapFromUrl(icon).get(10, TimeUnit.SECONDS);
          } catch (TimeoutException e) {
            Logger.e(
              TAG,
              "Timeout occurred whilst trying to retrieve a person icon: " + icon,
              e);
          } catch (Exception e) {
            Logger.e(
              TAG,
              "An error occurred whilst trying to retrieve a person icon: " + icon,
              e);
          }

          if (iconBitmap != null) {
            iconBitmap = ResourceUtils.getCircularBitmap(iconBitmap);
            shortcutInfoBuilder.setIcon(IconCompat.createWithAdaptiveBitmap(iconBitmap));
          }
        }

        String shortLabel = mNotificationAndroidShortcutBundle.getString("shortLabel");
        if (shortLabel != null && !shortLabel.isEmpty()) {
          shortcutInfoBuilder.setShortLabel(shortLabel);
        }

        String longLabel = mNotificationAndroidShortcutBundle.getString("longLabel");
        if (longLabel != null && !longLabel.isEmpty()) {
          shortcutInfoBuilder.setLongLabel(longLabel);
        }

        if (mNotificationAndroidShortcutBundle.containsKey("longLived")) {
          shortcutInfoBuilder.setLongLived(mNotificationAndroidShortcutBundle.getBoolean("longLived"));
        }

        ArrayList<Bundle> personBundles = mNotificationAndroidShortcutBundle.getParcelableArrayList("persons");
        if (personBundles != null && !personBundles.isEmpty()) {
          List<Person> persons = new ArrayList<>();
          for (Bundle personBundle : personBundles) {
            Person person =
              getPerson(
                lExecutor,
                Objects.requireNonNull(personBundle))
                .get(20, TimeUnit.SECONDS);

            if (person != null) {
              persons.add(person);
            }
          }
          shortcutInfoBuilder.setPersons(persons.toArray(new Person[0]));
        }

        return shortcutInfoBuilder;
      });
  }
}
