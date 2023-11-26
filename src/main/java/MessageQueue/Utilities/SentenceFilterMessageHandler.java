package MessageQueue.Utilities;

import java.util.HashMap;

public class SentenceFilterMessageHandler implements MessageHandler<String> {

    private static final String[] _questionWords = {"what", "which", "who", "where", "why", "when", "how", "whose"};
    private static final String[] _stopWords = {"a", "able", "about", "above", "abroad", "accordance",
            "according", "accordingly", "across", "act", "actually", "added",
            "adj", "adopted", "affected", "affecting", "affects", "after",
            "afterwards", "again", "against", "ago", "ah", "ahead", "ain't",
            "all", "allow", "allows", "almost", "alone", "along", "alongside",
            "already", "also", "although", "always", "am", "amid", "amidst",
            "among", "amongst", "amount", "an", "and", "announce", "another",
            "any", "anybody", "anyhow", "anymore", "anyone", "anything",
            "anyway", "anyways", "anywhere", "apart", "apparently", "appear",
            "appreciate", "appropriate", "approximately", "are", "aren", "arent",
            "aren't", "arise", "around", "as", "a's", "aside", "ask", "asking",
            "associated", "at", "auth", "available", "away", "awfully", "b", "back",
            "backward", "backwards", "be", "became", "because", "become", "becomes",
            "becoming", "been", "before", "beforehand", "begin", "beginning",
            "beginnings", "begins", "behind", "being", "believe", "below", "beside",
            "besides", "best", "better", "between", "beyond", "bill", "biol", "both",
            "bottom", "brief", "briefly", "but", "by", "c", "ca", "call", "came", "can",
            "cannot", "cant", "can't", "caption", "cause", "causes", "certain", "certainly",
            "changes", "clearly", "c'mon", "co", "co.", "com", "come", "comes", "computer",
            "con", "concerning", "consequently", "consider", "considering", "contain",
            "containing", "contains", "corresponding", "could", "couldnt", "couldn't",
            "course", "cry", "c's", "currently", "d", "dare", "daren't",
            "date", "de", "definitely", "describe", "described", "despite",
            "detail", "did", "didn't", "different", "directly", "do", "does",
            "doesn't", "doing", "done", "don't", "down", "downwards", "due",
            "during", "e", "each", "ed", "edu", "effect", "eg", "eight",
            "eighty", "either", "eleven", "else", "elsewhere", "empty",
            "end", "ending", "enough", "entirely", "especially", "et",
            "et-al", "etc", "even", "ever", "evermore", "every", "everybody",
            "everyone", "everything", "everywhere", "ex", "exactly", "example",
            "except", "f", "fairly", "far", "farther", "few", "fewer", "ff", "fifteen",
            "fifth", "fifty", "fill", "find", "fire", "first", "five", "fix", "followed",
            "following", "follows", "for", "forever", "former", "formerly", "forth", "forty",
            "forward", "found", "four", "from", "front", "full", "further", "furthermore", "g",
            "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes",
            "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "half", "happens",
            "hardly", "has", "hasnt", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll",
            "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "here's", "hereupon",
            "hers", "herself", "he's", "hi", "him", "himself", "his", "hither", "hopefully", "how",
            "howbeit", "however", "hundred", "i", "i'd", "ie", "if", "ignored", "i'll", "i'm", "immediate",
            "in", "inasmuch", "inc", "inc.", "indeed", "indicate", "indicated", "indicates", "inner",
            "inside", "insofar", "instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll",
            "its", "it's", "itself", "i've", "j", "just", "k", "keep", "keeps", "kept", "know", "known",
            "knows", "l", "last", "lately", "later", "latter", "latterly", "least", "less", "lest",
            "let", "let's", "like", "liked", "likely", "likewise", "little", "look", "looking", "looks",
            "low", "lower", "ltd", "m", "made", "mainly", "make", "makes", "many", "may", "maybe",
            "mayn't", "me", "mean", "meantime", "meanwhile", "merely", "might", "mightn't", "mine",
            "minus", "miss", "more", "moreover", "most", "mostly", "mr", "mrs", "much", "must",
            "mustn't", "my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary",
            "need", "needn't", "needs", "neither", "never", "neverf", "neverless", "nevertheless",
            "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone",
            "no-one", "nor", "normally", "not", "nothing", "notwithstanding", "novel", "now",
            "nowhere", "o", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on",
            "once", "one", "ones", "one's", "only", "onto", "opposite", "or", "other", "others",
            "otherwise", "ought", "oughtn't", "our", "ours", "ourselves", "out", "outside", "over",
            "overall", "own", "p", "particular", "particularly", "past", "per", "perhaps", "placed",
            "please", "plus", "possible", "presumably", "probably", "provided", "provides", "q", "que",
            "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably", "recent", "recently",
            "regarding", "regardless", "regards", "relatively", "respectively", "right", "round", "s",
            "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing",
            "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent",
            "serious", "seriously", "seven", "several", "shall", "shan't", "she", "she'd", "she'll",
            "she's", "should", "shouldn't", "since", "six", "so", "some", "somebody", "someday",
            "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere",
            "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup",
            "sure", "t", "take", "taken", "taking", "tell", "tends", "th", "than", "thank", "thanks",
            "thanx", "that", "that'll", "thats", "that's", "that've", "the", "their", "theirs",
            "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "there'd",
            "therefore", "therein", "there'll", "there're"};

    public static HashMap<String, Boolean> arrayToDict(String[] array) {
        HashMap<String, Boolean> dict = new HashMap<String, Boolean>();
        for (String element : array) {
            dict.put(element, true);
        }
        return dict;
    }

    public static HashMap<String, Boolean> STOP_WORDS = arrayToDict(_stopWords);
    public static HashMap<String, Boolean> QUESTION_WORDS = arrayToDict(_questionWords);

    private Message<String> _message;

    @Override
    public void setMessage(Message<String> message) {
        _message = message;
    }

    private String result = "";

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void handle() {
        for (String word : _message.payload.split(" ")) {
            if (STOP_WORDS.get(word) != null || QUESTION_WORDS.get(word) != null) {
                System.out.println(word);
                result =  result.concat(word) + " ";
            }
        }
    }
}
