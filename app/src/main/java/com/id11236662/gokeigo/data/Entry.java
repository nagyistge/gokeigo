package com.id11236662.gokeigo.data;

import com.id11236662.gokeigo.util.AppDatabase;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * This class represents a container that holds a respectful/polite/humble word, its definition,
 * and a link to the polite version of the word if it isn't the polite version already.
 */
@Table(database = AppDatabase.class, allFields = true)
public class Entry extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    //@ForeignKey// TODO: Figure out how to add foreign key
    private Long politeWordId;

    private String wordInKanjiKana;
    private String wordInLowerCaseRomaji;
    private int level;
    private String definition;
    private String note;

    public Entry() {
        // Required empty public constructor so the table can be created
    }

    public Entry(String wordInKanjiKana, String wordInRomaji, int level, String definition) {
        this.wordInKanjiKana = wordInKanjiKana;
        // Set the romaji in lower case for easier search filtering
        wordInLowerCaseRomaji = wordInRomaji.toLowerCase();
        this.level = level;
        this.definition = definition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPoliteWordId() {
        return politeWordId;
    }

    public void setPoliteWordId(Long politeWordId) {
        this.politeWordId = politeWordId;
    }

    public String getWordInKanjiKana() {
        return wordInKanjiKana;
    }

    public void setWordInKanjiKana(String wordInKanjiKana) {
        this.wordInKanjiKana = wordInKanjiKana;
    }

    public String getWordInLowerCaseRomaji() {
        return wordInLowerCaseRomaji;
    }

    public void setWordInLowerCaseRomaji(String wordInLowerCaseRomaji) {
        this.wordInLowerCaseRomaji = wordInLowerCaseRomaji;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        // TODO: DBFlow inserts it's literal value into the ModelAdapter for the table so this String must be escaped before setting
    }
}
