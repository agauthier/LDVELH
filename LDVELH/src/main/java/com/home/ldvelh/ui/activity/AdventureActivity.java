package com.home.ldvelh.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Pair;
import android.widget.TextView;

import com.home.ldvelh.AdventureConfig;
import com.home.ldvelh.R;
import com.home.ldvelh.commons.GameSaver;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.Character;
import com.home.ldvelh.ui.dialog.AdventureDialog;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog.Choice;
import com.home.ldvelh.ui.page.PagesAdapter;
import com.home.ldvelh.ui.widget.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class AdventureActivity extends AbstractGameActivity implements Observer {

    AdventureConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            config = (AdventureConfig) getIntent().getExtras().get(AdventureConfig.ADVENTURE_CONFIG);
        } else {
            config = (AdventureConfig) savedInstanceState.get(AdventureConfig.ADVENTURE_CONFIG);
        }
        if (config != null) {
            config.loadGame();
            setContentView(config.getLayoutResId());
            setBookTitle(config.getTitleResId());
            showStartupDialogs();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Property.CHARACTER.get().addLifeObserver(this);
        GameSaver.start(config);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameSaver.stop();
        Property.CHARACTER.get().deleteObserver(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        AdventureDialog.dismissCurrentDialog();
        outState.putSerializable(AdventureConfig.ADVENTURE_CONFIG, config);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MultipleChoiceDialog backPressedDialog = new MultipleChoiceDialog(this) {
            @Override
            protected Pair<Integer, List<Choice>> getChoices() {
                return getEndingChoices(R.string.back_pressed, true);
            }
        };
        backPressedDialog.show();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((Character) observable).isDead()) {
            MultipleChoiceDialog characterDeadDialog = new MultipleChoiceDialog(this) {
                @Override
                protected Pair<Integer, List<Choice>> getChoices() {
                    return getEndingChoices(R.string.character_dead, false);
                }
            };
            characterDeadDialog.show();
        }
    }

    private void setBookTitle(int titleResId) {
        TextView bookTitle = findViewById(R.id.bookTitle);
        bookTitle.setText(titleResId);
    }

    private <T extends Fragment> void initPages(final List<PageInfo<T>> pages) {
        ViewPager viewPager = findViewById(R.id.pages);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        if (viewPager != null && tabLayout != null) {
            final PagesAdapter pagesAdapter = new PagesAdapter(getSupportFragmentManager(), pages);
            viewPager.setAdapter(pagesAdapter);
            viewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    PagesAdapter.setSelectedPage(position);
                    notifyObservers();
                }
            });
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void showStartupDialogs() {
        showDialogs(config.getUnfulfilledDialogs());
    }

    private <T extends AdventureDialog> void showDialogs(final List<Class<T>> unfulfilledDialogs) {
        if (unfulfilledDialogs.isEmpty()) {
            initPages(config.getPages());
        } else {
            try {
                final Class<T> dialogClass = unfulfilledDialogs.get(0);
                final AdventureDialog dialog = dialogClass.getConstructor(Context.class, Object.class).newInstance(this, config.getDialogData(dialogClass));
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        List<Class<T>> remainingDialogs = new ArrayList<>(unfulfilledDialogs);
                        if (dialog.isFulfilled()) {
                            Property.CHARACTER.get().fulfillDialog(dialogClass);
                        }
                        remainingDialogs.remove(dialogClass);
                        showDialogs(remainingDialogs);
                    }
                });
                dialog.show();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private Pair<Integer, List<Choice>> getEndingChoices(int questionResId, boolean resumeOptionAvailable) {
        List<Choice> choices = new ArrayList<>();
        final AdventureActivity activity = this;
        choices.add(new Choice(R.string.restart_adventure, new Runnable() {
            @Override
            public void run() {
                Property.CHARACTER.get().killWithoutUpdate();
                removeActivity(activity);
                finish();
                config.launch();
            }
        }));
        choices.add(new Choice(R.string.play_other_book, new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }));
        choices.add(new Choice(R.string.quit, new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }));
        if (resumeOptionAvailable) {
            choices.add(new Choice(R.string.cancel, new Runnable() {
                @Override
                public void run() {}
            }));
        }
        return new Pair<>(questionResId, choices);
    }
}
